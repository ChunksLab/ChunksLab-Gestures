package com.chunkslab.gestures.nms.v1_21_R2;

import com.chunkslab.gestures.nms.api.WardrobeNMS;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.Optionull;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetCameraPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.UUID;

public class WardrobeImpl implements WardrobeNMS {

    private ServerPlayer npc;
    private int entityID;

    @Override
    public void spawn(Player player, Location location) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld)location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        String[] skinArray = getSkinFromPlayer(player);
        gameProfile.getProperties().replaceValues("textures", ImmutableList.of(new Property("textures", skinArray[0], skinArray[1])));
        ServerPlayer npc = new ServerPlayer(minecraftServer, serverLevel, gameProfile, ClientInformation.createDefault());
        this.npc = npc;
        this.entityID = npc.getId();
        EnumSet<ClientboundPlayerInfoUpdatePacket.Action> actions = EnumSet.noneOf(ClientboundPlayerInfoUpdatePacket.Action.class);
        actions.add(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER);
        ClientboundPlayerInfoUpdatePacket playerInfoPacket = new ClientboundPlayerInfoUpdatePacket(actions, getEntry());
        serverPlayer.connection.send(playerInfoPacket);
        ClientboundAddEntityPacket addEntityPacket = new ClientboundAddEntityPacket(
                entityID,
                gameProfile.getId(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw(),
                npc.getType(),
                0,
                Vec3.ZERO,
                0
        );
        serverPlayer.connection.send(addEntityPacket);
    }

    @Override
    public void destroy(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(entityID);
        serverPlayer.connection.send(packet);
    }

    private ClientboundPlayerInfoUpdatePacket.Entry getEntry() {
        GameProfile profile = npc.getGameProfile();

        return new ClientboundPlayerInfoUpdatePacket.Entry(
                npc.getUUID(),
                profile,
                false,
                69,
                npc.gameMode.getGameModeForPlayer(),
                npc.getTabListDisplayName(),
                -1,
                Optionull.map(npc.getChatSession(), RemoteChatSession::asData)
        );
    }

    private String[] getSkinFromPlayer(Player player) throws NoSuchElementException {
        ServerPlayer serverPlayer = ((CraftPlayer)player).getHandle();
        GameProfile profile = serverPlayer.getBukkitEntity().getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.value();
        String signature = property.signature();
        return new String[]{texture, signature};
    }
}