package com.chunkslab.gestures.nms.v1_21_R2;

import com.chunkslab.gestures.nms.api.MountNMS;
import com.chunkslab.gestures.nms.api.util.UnsafeFunction;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class MountImpl implements MountNMS {

    private Horse horse;

    @Override
    public void spawn(Player player) {
        player.setInvisible(true);
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        if (horse == null) {
            horse = new Horse(EntityType.HORSE, ((CraftWorld)player.getWorld()).getHandle());
            horse.setInvisible(true);
            horse.setHealth(0);
            horse.setInvulnerable(true);
            horse.setTamed(true);
            horse.getAttribute(CraftAttribute.bukkitToMinecraftHolder(Attribute.MAX_HEALTH)).setBaseValue(0);
        }
        horse.setPos(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());

        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(
                horse.getId(),
                horse.getUUID(),
                horse.getX(),
                horse.getY(),
                horse.getZ(),
                0,
                0,
                horse.getType(),
                0,
                Vec3.ZERO,
                0
        );
        ClientboundSetEntityDataPacket dataPacket = new ClientboundSetEntityDataPacket(
                horse.getId(),
                horse.getEntityData().getNonDefaultValues()
        );
        ClientboundUpdateAttributesPacket attributesPacket = new ClientboundUpdateAttributesPacket(
                horse.getId(),
                horse.getAttributes().getSyncableAttributes()
        );
        int[] passengers = new int[serverPlayer.getPassengers().size() + 1];
        passengers[0] = serverPlayer.getId();
        int i = 1;
        for (Entity entity : serverPlayer.getPassengers()) {
            passengers[i] = entity.getId();
            i++;
        }
        ClientboundSetPassengersPacket passengersPacket = createDataSerializer(friendlyByteBuf -> {
            friendlyByteBuf.writeVarInt(horse.getId());
            friendlyByteBuf.writeVarIntArray(passengers);
            return ClientboundSetPassengersPacket.STREAM_CODEC.decode(friendlyByteBuf);
        });
        List<Pair<EquipmentSlot, ItemStack>> emptyEquipment = new ArrayList<>();
        for(EquipmentSlot slot : EquipmentSlot.values())
            emptyEquipment.add(Pair.of(slot, ItemStack.EMPTY));
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), emptyEquipment);
        serverPlayer.connection.send(addEntityPacket);
        serverPlayer.connection.send(dataPacket);
        serverPlayer.connection.send(attributesPacket);
        serverPlayer.connection.send(passengersPacket);
        serverPlayer.connection.send(equipmentPacket);
    }

    @Override
    public void destroy(Player player) {
        player.setInvisible(false);
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ClientboundRemoveEntitiesPacket removeEntitiesPacket = new ClientboundRemoveEntitiesPacket(horse.getId());
        serverPlayer.connection.send(removeEntitiesPacket);
        List<Pair<EquipmentSlot, ItemStack>> equipment = new ArrayList<>();
        for(EquipmentSlot slot : EquipmentSlot.values())
            equipment.add(Pair.of(slot, serverPlayer.getItemBySlot(slot)));
        ClientboundSetEquipmentPacket equipmentPacket = new ClientboundSetEquipmentPacket(player.getEntityId(), equipment);
        serverPlayer.connection.send(equipmentPacket);
    }

    @Override
    public LivingEntity getBukkitEntity() {
        return horse.getBukkitLivingEntity();
    }

    private <T> T createDataSerializer(UnsafeFunction<FriendlyByteBuf, T> callback) {
        FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
        T result = null;
        try {
            result = callback.apply(data);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            data.release();
        }
        return result;
    }
}

