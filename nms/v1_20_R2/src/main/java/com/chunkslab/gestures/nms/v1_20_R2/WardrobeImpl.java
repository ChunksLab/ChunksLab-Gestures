package com.chunkslab.gestures.nms.v1_20_R2;

import com.chunkslab.gestures.nms.api.WardrobeNMS;
import com.mojang.authlib.GameProfile;
import net.minecraft.Optionull;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.EnumSet;
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
                Optionull.map(npc.getChatSession(), RemoteChatSession::asData)
        );
    }
}