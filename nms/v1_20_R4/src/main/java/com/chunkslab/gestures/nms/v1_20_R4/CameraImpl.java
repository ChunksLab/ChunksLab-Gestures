package com.chunkslab.gestures.nms.v1_20_R4;

import com.chunkslab.gestures.nms.api.CameraNMS;
import com.chunkslab.gestures.nms.api.util.ReflectionUtils;
import com.chunkslab.gestures.nms.api.util.SelfIncreaseEntityID;
import com.google.common.collect.Lists;
import net.minecraft.Optionull;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CameraImpl implements CameraNMS {
    
    private static final Method sendPacketImmediateMethod;

    private final int entityID = SelfIncreaseEntityID.getAndIncrease();
    private ArmorStand armorStand;

    static {
        sendPacketImmediateMethod = ReflectionUtils.getDeclaredMethod(Connection.class, void.class, Packet.class, PacketSendListener.class, boolean.class);
    }

    @Override
    public void spawn(Player player, Location location) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        if (armorStand == null) {
            armorStand = new ArmorStand(EntityType.ARMOR_STAND, serverPlayer.serverLevel());
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
                location.getYaw()
        );
        serverPlayer.connection.send(entityPacket);
        ArrayList<SynchedEntityData.DataValue<?>> values = new ArrayList<>();
        // Invisible
        values.add(SynchedEntityData.DataValue.create(new EntityDataAccessor<>(0, EntityDataSerializers.BYTE), (byte) (0x20)));
        // Small
        //values.add(SynchedEntityData.DataValue.create(new EntityDataAccessor<>(15, EntityDataSerializers.BYTE), (byte) 0x01));
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(entityID, values));
        serverPlayer.connection.send(new ClientboundSetCameraPacket(armorStand));
        player.setGameMode(GameMode.SPECTATOR);
        ClientboundPlayerInfoUpdatePacket spectatorPacket = new ClientboundPlayerInfoUpdatePacket(Enum.valueOf(ClientboundPlayerInfoUpdatePacket.Action.class, "UPDATE_GAME_MODE"), serverPlayer);
        try {
            Field packetField = spectatorPacket.getClass().getDeclaredField("c");
            packetField.setAccessible(true);
            ArrayList<ClientboundPlayerInfoUpdatePacket.Entry> list = Lists.newArrayList();
            list.add(new ClientboundPlayerInfoUpdatePacket.Entry(player.getUniqueId(), serverPlayer.getBukkitEntity().getProfile(), false, 0, GameType.CREATIVE, serverPlayer.listName, Optionull.map(serverPlayer.getChatSession(), RemoteChatSession::asData)));
            packetField.set(spectatorPacket, list);
            ClientboundGameEventPacket gameEventPacket = new ClientboundGameEventPacket(ClientboundGameEventPacket.CHANGE_GAME_MODE, 3.0f);
            serverPlayer.connection.send(spectatorPacket);
            serverPlayer.connection.send(gameEventPacket);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void destroy(Player player) {
        player.setGameMode(player.getPreviousGameMode() == null ? GameMode.SURVIVAL : player.getPreviousGameMode());
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.send(new ClientboundSetCameraPacket(serverPlayer));
        serverPlayer.connection.send(new ClientboundRemoveEntitiesPacket(entityID));
    }

    @Override
    public void title(Player player, String text) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ArrayList<Packet<ClientGamePacketListener>> packetListeners = new ArrayList<>();
        packetListeners.add(new ClientboundSetTitlesAnimationPacket(15, 7, 15));
        MutableComponent title = Component.nullToEmpty(text).copy();
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
