/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) Amownyy <amowny08@gmail.com>
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

package com.chunkslab.gestures.playeranimator.nms.v1_20_R4;

import com.chunkslab.gestures.playeranimator.api.nms.INMSHandler;
import com.chunkslab.gestures.playeranimator.api.nms.IRangeManager;
import com.chunkslab.gestures.playeranimator.api.nms.IRenderer;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import com.chunkslab.gestures.playeranimator.nms.v1_20_R4.entity.RangeManager;
import com.chunkslab.gestures.playeranimator.nms.v1_20_R4.entity.RendererImpl;
import com.chunkslab.gestures.playeranimator.nms.v1_20_R4.network.PAChannelHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelPipeline;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class v1_20_R4 implements INMSHandler {

    @Override
    public void injectPlayer(Player player) {
        ServerPlayer ply = ((CraftPlayer) player).getHandle();
        PAChannelHandler cdh = new PAChannelHandler(ply);

        ChannelPipeline pipeline = ply.connection.connection.channel.pipeline();
        for (String name : pipeline.toMap().keySet()) {
            if (pipeline.get(name) instanceof Connection) {
                pipeline.addBefore(name, "player_animator_packet_handler", cdh);
                break;
            }
        }
    }

    @Override
    public void removePlayer(Player player) {
        Channel channel = ((CraftPlayer) player).getHandle().connection.connection.channel;
        channel.eventLoop().submit(() -> {
            channel.pipeline().remove("player_animator_packet_handler");
            return null;
        });
    }

    @Override
    public IRangeManager createRangeManager(Entity entity) {
        try {
            net.minecraft.world.entity.Entity nmsEntity = ((CraftEntity) entity).getHandle();

            Field trackedEntityField = net.minecraft.world.entity.Entity.class.getDeclaredField("trackedEntity");
            trackedEntityField.setAccessible(true);
            Object trackedEntity = trackedEntityField.get(nmsEntity);

            return new RangeManager(trackedEntity);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public IRenderer createRenderer() {
        return new RendererImpl();
    }

    @Override
    public String getTexture(Player player) {
        return ((CraftPlayer) player).getHandle().getGameProfile().getProperties().get("textures").iterator().next().value();
    }

    @Override
    public ItemStack setSkullTexture(ItemStack skull, TextureWrapper texture) {
        if (skull == null)
            skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        try {
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(UUID.randomUUID());
            playerProfile.getTextures().setSkin(new URL(texture.getUrl()), texture.isSlim() ? PlayerTextures.SkinModel.SLIM : PlayerTextures.SkinModel.CLASSIC);
            skullMeta.setOwnerProfile(playerProfile);
            skull.setItemMeta(skullMeta);
            return skull;
        } catch (MalformedURLException e) {
            return skull;
        }
    }

}