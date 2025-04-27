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

package com.chunkslab.gestures.playeranimator.nms.v1_18_R1;

import com.chunkslab.gestures.playeranimator.api.nms.INMSHandler;
import com.chunkslab.gestures.playeranimator.api.nms.IRangeManager;
import com.chunkslab.gestures.playeranimator.api.nms.IRenderer;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.chunkslab.gestures.playeranimator.nms.v1_18_R1.entity.RangeManager;
import com.chunkslab.gestures.playeranimator.nms.v1_18_R1.entity.RendererImpl;
import com.chunkslab.gestures.playeranimator.nms.v1_18_R1.network.PAChannelHandler;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class v1_18_R1 implements INMSHandler {

    @Override
    public void injectPlayer(Player player) {
        ServerPlayer ply = ((CraftPlayer) player).getHandle();
        PAChannelHandler cdh = new PAChannelHandler(ply);

        ChannelPipeline pipeline = ply.connection.getConnection().channel.pipeline();
        for (String name : pipeline.toMap().keySet()) {
            if (pipeline.get(name) instanceof Connection) {
                pipeline.addBefore(name, "player_animator_packet_handler", cdh);
                break;
            }
        }
    }

    @Override
    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.getConnection().channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("player_animator_packet_handler");
            return null;
        });
    }

    @Override
    public IRangeManager createRangeManager(Entity entity) {
        ServerLevel level = ((CraftWorld) entity.getWorld()).getHandle();
        ChunkMap.TrackedEntity trackedEntity = level.getChunkSource().chunkMap.entityMap.get(entity.getEntityId());
        return new RangeManager(trackedEntity);
    }

    @Override
    public IRenderer createRenderer() {
        return new RendererImpl();
    }

    @Override
    public String getTexture(Player player) {
        return ((CraftPlayer) player).getHandle().getGameProfile().getProperties().get("textures").iterator().next().getValue();
    }

    @Override
    public ItemStack setSkullTexture(ItemStack skull, TextureWrapper texture) {
        if (skull == null)
            skull = new ItemStack(Material.PLAYER_HEAD);
        final var nmsSkull = CraftItemStack.asNMSCopy(skull);
        final var tag = nmsSkull.getOrCreateTag();

        final var profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture.toBase64()));
        tag.put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), profile));

        nmsSkull.setTag(tag);

        return CraftItemStack.asBukkitCopy(nmsSkull);
    }

}