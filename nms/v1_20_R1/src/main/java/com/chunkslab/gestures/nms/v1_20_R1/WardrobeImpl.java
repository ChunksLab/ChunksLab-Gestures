/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chunkslab.gestures.nms.v1_20_R1;

import com.chunkslab.gestures.nms.api.WardrobeNMS;
import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.RemoteChatSession;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.NoSuchElementException;
import java.util.UUID;

public class WardrobeImpl implements WardrobeNMS {

    private ServerPlayer npc;
    private Slime slime;
    private int entityID;

    @Override
    public void spawn(Player player, Location location) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        MinecraftServer minecraftServer = ((CraftServer) Bukkit.getServer()).getServer();
        ServerLevel serverLevel = ((CraftWorld)location.getWorld()).getHandle();
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), player.getName());
        String[] skinArray = getSkinFromPlayer(player);
        gameProfile.getProperties().replaceValues("textures", ImmutableList.of(new Property("textures", skinArray[0], skinArray[1])));
        ServerPlayer npc = new ServerPlayer(minecraftServer, serverLevel, gameProfile);
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
        Slime slime = new Slime(EntityType.SLIME, serverLevel);
        this.slime = slime;
        ClientboundAddEntityPacket slimeAddEntityPacket = new ClientboundAddEntityPacket(
                slime.getId(),
                slime.getUUID(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw(),
                slime.getType(),
                0,
                Vec3.ZERO,
                0
        );
        serverPlayer.connection.send(slimeAddEntityPacket);
        ArrayList<SynchedEntityData.DataValue<?>> values = new ArrayList<>();
        values.add(SynchedEntityData.DataValue.create(new EntityDataAccessor<>(0, EntityDataSerializers.BYTE), (byte) (0x20)));
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(entityID, values));
        serverPlayer.connection.send(new ClientboundSetEntityDataPacket(slime.getId(), values));
        serverPlayer.connection.send(new ClientboundSetPassengersPacket(slime));
        IntArrayList players = new IntArrayList();
        for (Player p : player.getWorld().getPlayers()) {
            if (player.getUniqueId().equals(p.getUniqueId())) continue;
            players.add(p.getEntityId());
        }
        ClientboundRemoveEntitiesPacket removePlayersPacket = new ClientboundRemoveEntitiesPacket(players);
        serverPlayer.connection.send(removePlayersPacket);
    }

    @Override
    public void destroy(Player player) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        ClientboundRemoveEntitiesPacket packet = new ClientboundRemoveEntitiesPacket(entityID, slime.getId());
        serverPlayer.connection.send(packet);
        for (Player p : player.getWorld().getPlayers()) {
            if (player.getUniqueId().equals(p.getUniqueId())) continue;
            ClientboundAddEntityPacket addPlayerPacket = new ClientboundAddEntityPacket(((CraftPlayer) p).getHandle(), 0, BlockPos.ZERO);
            serverPlayer.connection.send(addPlayerPacket);
        }
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

    private String[] getSkinFromPlayer(Player player) throws NoSuchElementException {
        ServerPlayer serverPlayer = ((CraftPlayer)player).getHandle();
        GameProfile profile = serverPlayer.getBukkitEntity().getProfile();
        Property property = profile.getProperties().get("textures").iterator().next();
        String texture = property.getValue();
        String signature = property.getSignature();
        return new String[]{texture, signature};
    }
}