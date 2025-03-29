package com.chunkslab.gestures.nms.v1_21_R2;

import com.chunkslab.gestures.nms.api.MountNMS;
import com.chunkslab.gestures.nms.api.ThirdPersonNMS;
import com.mojang.authlib.GameProfile;
import lombok.SneakyThrows;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PositionMoveRotation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class ThirdPersonImpl implements ThirdPersonNMS {

    @SneakyThrows
    @Override
    public Object spawn(Player player, Object playerModel, MountNMS mountNMS) {
        Location location = player.getLocation();
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld)location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName() + "ThirdPerson");
        ServerPlayer thirdPersonPlayer = new ServerPlayer(minecraftServer, serverLevel, gameProfile, ClientInformation.createDefault());
        thirdPersonPlayer.setInvisible(true);
        thirdPersonPlayer.setInvulnerable(true);
        mountNMS.spawn(player);
        ClientboundAddEntityPacket spawnThirdPersonPlayer = new ClientboundAddEntityPacket(thirdPersonPlayer, 0, CraftLocation.toBlockPosition(location));
        serverPlayer.connection.send(spawnThirdPersonPlayer);
        ClientboundPlayerInfoUpdatePacket thirdPersonPlayerInfoPacket = new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, thirdPersonPlayer);
        serverPlayer.connection.send(thirdPersonPlayerInfoPacket);
        ClientboundSetCameraPacket thirdPersonCamera = new ClientboundSetCameraPacket(thirdPersonPlayer);
        serverPlayer.connection.send(thirdPersonCamera);
        for (Object obj : (Iterable<?>) playerModel.getClass().getMethod("getSeenBy").invoke(playerModel)) {
            ServerPlayer seenPlayer = ((CraftPlayer) obj).getHandle();
            if (seenPlayer.getUUID().equals(player.getUniqueId())) continue;
            //seenPlayer.connection.send();
        }
        return thirdPersonPlayer;
    }

    @SneakyThrows
    @Override
    public void destroy(Player player, Object thirdPersonPlayerObject, Object playerModel, MountNMS mountNMS) {
        ServerPlayer thirdPersonPlayer = (ServerPlayer) thirdPersonPlayerObject;
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ClientboundRemoveEntitiesPacket removeThirdPersonPlayer = new ClientboundRemoveEntitiesPacket(thirdPersonPlayer.getId());
        serverPlayer.connection.send(removeThirdPersonPlayer);
        mountNMS.destroy(player);
        for (Object obj : (Iterable<?>) playerModel.getClass().getMethod("getSeenBy").invoke(playerModel)) {
            ServerPlayer seenPlayer = ((CraftPlayer) obj).getHandle();
            if (seenPlayer.getUUID().equals(player.getUniqueId())) continue;
            seenPlayer.connection.send(removeThirdPersonPlayer);
        }
    }

    @Override
    public void run(Player player, Object thirdPersonPlayerObject) {
        ServerPlayer thirdPersonPlayer = (ServerPlayer) thirdPersonPlayerObject;
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        double distanceInFrontOfPlayer = -3.5;
        double offset = 0;
        Vector direction = player.getLocation().getDirection().multiply(distanceInFrontOfPlayer);
        Location location = player.getLocation();
        Location front = location.add(direction);
        if (front.getBlock().getType().isSolid()) {
            Location checkLoc = front.clone();
            while (offset <= Math.abs(distanceInFrontOfPlayer) && !checkLoc.getBlock().getType().isAir()) {
                offset += 0.1;
                checkLoc = location.clone().add(location.getDirection().multiply(-(distanceInFrontOfPlayer + offset)));
            }
            front = checkLoc;
        }
        //thirdPersonPlayer.moveTo(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        ClientboundRotateHeadPacket rotateHeadPacket = new ClientboundRotateHeadPacket(thirdPersonPlayer, (byte) (front.getYaw() * 256.0f / 360.0f));
        ClientboundMoveEntityPacket moveEntityPacket = new ClientboundMoveEntityPacket.Rot(thirdPersonPlayer.getId(), (byte)(front.getYaw() * 256.0f / 360.0f), (byte)(front.getPitch() * 256.0f / 360.0f), true);
        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(
                thirdPersonPlayer.getId(),
                PositionMoveRotation.of(thirdPersonPlayer),
                Set.of(),
                false
        );
        serverPlayer.connection.send(rotateHeadPacket);
        serverPlayer.connection.send(moveEntityPacket);
        serverPlayer.connection.send(teleportEntityPacket);
    }

    @SneakyThrows
    @Override
    public void moveMounted(Player player, Object playerModel, MountNMS mountNMS) {
        Method getGestureMethod = playerModel.getClass().getMethod("getGesture");
        Object gesture = getGestureMethod.invoke(playerModel);

        Method getMoveSpeedMethod = gesture.getClass().getMethod("getMoveSpeed");
        double speed = (double) getMoveSpeedMethod.invoke(gesture);
        Vector direction = player.getLocation().getDirection().normalize().multiply(speed);
        Vector velocity = direction.setY(mountNMS.getBukkitEntity().getVelocity().getY());
        mountNMS.getBukkitEntity().setVelocity(velocity);
    }

    @SneakyThrows
    @Override
    public void rotateMounted(Object playerModel, boolean rotation) {
        Method setRotateMethod = playerModel.getClass().getMethod("setRotate", boolean.class);
        setRotateMethod.invoke(playerModel, true);
    }
}