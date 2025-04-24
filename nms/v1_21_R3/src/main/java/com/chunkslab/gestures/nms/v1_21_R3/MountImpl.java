package com.chunkslab.gestures.nms.v1_21_R3;

import com.chunkslab.gestures.nms.api.MountNMS;
import com.chunkslab.gestures.nms.api.util.UnsafeFunction;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.portal.TeleportTransition;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MountImpl implements MountNMS {

    private Horse horse;
    private final Map<Player, Location> runningPlayers = Maps.newConcurrentMap();

    @Override
    public void spawn(Player player) {
        player.setInvisible(true);
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        horse = new Horse(EntityType.HORSE, ((CraftWorld)player.getWorld()).getHandle());
        horse.setNoGravity(true);
        horse.setInvisible(true);
        horse.setInvulnerable(true);
        horse.setTamed(true);
        horse.getAttribute(CraftAttribute.bukkitToMinecraftHolder(Attribute.MAX_HEALTH)).setBaseValue(0);
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
        runningPlayers.put(player, player.getLocation());
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
        player.teleport(runningPlayers.remove(player));
    }

    @Override
    public void run(Player player, Location centerLocation) {
        if (!runningPlayers.containsKey(player)) return;
        centerLocation.add(0, 0.95, 0);
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        Vector playerDirection = player.getLocation().getDirection();
        Location lastLocation = centerLocation.clone().subtract(playerDirection.clone().multiply(0.1));
        for (double i = 0.4; i <= 3.5; i = i + 0.1) {
            Location maxA = lastLocation.clone().add(new Vector(0.35, 0.25, 0.35));
            Location minA = lastLocation.clone().add(new Vector(-0.35, 0.25, 0.35));
            Location minB = lastLocation.clone().add(new Vector(0.35, 0.25, -0.35));
            Location maxB = lastLocation.clone().add(new Vector(-0.35, 0.25, -0.35));
            if (maxA.getBlock().isSolid() || minA.getBlock().isSolid() || minB.getBlock().isSolid() || maxB.getBlock().isSolid()) { break; }
            maxA.subtract(0, 1, 0);
            minA.subtract(0, 1, 0);
            minB.subtract(0, 1, 0);
            maxB.subtract(0, 1, 0);
            if (maxA.getBlock().isSolid() || minA.getBlock().isSolid() || minB.getBlock().isSolid() || maxB.getBlock().isSolid()) { break; }
            lastLocation = centerLocation.clone().subtract(playerDirection.clone().multiply(i));
        }
        Location teleportLocation = lastLocation.subtract(new Vector(0, player.getEyeHeight(), 0));
        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(
                horse.getId(),
                PositionMoveRotation.of(new TeleportTransition(
                        serverPlayer.serverLevel(),
                        new Vec3(teleportLocation.getX(), teleportLocation.getY(), teleportLocation.getZ()),
                        Vec3.ZERO,
                        teleportLocation.getYaw(), teleportLocation.getPitch(),
                        false, false, Set.of(), (entity -> {}), PlayerTeleportEvent.TeleportCause.PLUGIN
                )),
                Set.of(),
                false
        );
        serverPlayer.connection.send(teleportEntityPacket);
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

