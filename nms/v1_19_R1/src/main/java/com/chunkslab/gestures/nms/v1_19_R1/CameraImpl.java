package com.chunkslab.gestures.nms.v1_19_R1;

import com.chunkslab.gestures.nms.api.CameraNMS;
import com.chunkslab.gestures.nms.api.util.SelfIncreaseEntityID;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

public class CameraImpl implements CameraNMS {
    
    private static final Method sendPacketImmediateMethod;

    private final int entityID = SelfIncreaseEntityID.getAndIncrease();
    private ArmorStand armorStand;

    static {
        try {
            sendPacketImmediateMethod = Connection.class.getDeclaredMethod("sendPacket", Packet.class, PacketSendListener.class, Boolean.class);
            sendPacketImmediateMethod.setAccessible(true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to get send packet method", e);
        }
    }

    @Override
    public void spawn(Player player, Location location) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        if (armorStand == null) {
            armorStand = new ArmorStand(EntityType.ARMOR_STAND, serverPlayer.level);
            armorStand.setId(entityID);
        }
        ClientboundAddEntityPacket entityPacket = new ClientboundAddEntityPacket(
                entityID,
                armorStand.getUUID(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw(),
                EntityType.ARMOR_STAND,
                0,
                Vec3.ZERO,
                0
        );
        serverPlayer.connection.send(entityPacket);
        SynchedEntityData entityData = new SynchedEntityData(armorStand);
        // Invisible
        entityData.define(new EntityDataAccessor<>(0, EntityDataSerializers.BYTE), (byte) (0x20));
        // Small
        entityData.define(new EntityDataAccessor<>(15, EntityDataSerializers.BYTE), (byte) 0x01);
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(entityID, entityData, true));
        serverPlayer.connection.send(new ClientboundSetCameraPacket(armorStand));
    }

    @Override
    public void destroy(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(new ClientboundSetCameraPacket(serverPlayer));
        serverPlayer.connection.send(new ClientboundRemoveEntitiesPacket(entityID));
    }

    @Override
    public void title(Player player, String text) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ArrayList<Packet<ClientGamePacketListener>> packetListeners = new ArrayList<>();
        packetListeners.add(new ClientboundSetTitlesAnimationPacket(15, 7, 15));
        MutableComponent title = Component.nullToEmpty(text).copy().withStyle(Style.EMPTY.withFont(new ResourceLocation("gestures/gestures")));
        packetListeners.add(new ClientboundSetTitleTextPacket(title));
        for (Packet<ClientGamePacketListener> packet: packetListeners) {
            sendPacketImmediately(serverPlayer, packet);
        }
    }

    private void sendPacketImmediately(ServerPlayer serverPlayer, Packet<ClientGamePacketListener> packet) {
        try {
            sendPacketImmediateMethod.invoke(serverPlayer.connection.connection, packet, null, true);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Failed to send packet", e);
        }
    }
}
