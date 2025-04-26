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

package com.chunkslab.gestures.playeranimator.nms.v1_18_R1.network;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.model.player.PlayerModel;
import com.chunkslab.gestures.playeranimator.api.utils.FieldUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.Getter;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import org.bukkit.entity.Entity;

public class PAChannelHandler extends ChannelDuplexHandler {

    @Getter
    private final ServerPlayer player;

    public PAChannelHandler(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        if(msg instanceof ClientboundAddEntityPacket packet) {
            handleEntitySpawn(packet.getId());
        }else if(msg instanceof ClientboundAddMobPacket packet) {
            handleEntitySpawn(packet.getId());
        }else if(msg instanceof ClientboundAddPlayerPacket packet) {
            handleEntitySpawn(packet.getEntityId());
        }else if(msg instanceof ClientboundRemoveEntitiesPacket packet) {
            for(int id : packet.getEntityIds()) {
                handleEntityDespawn(id);
            }
        }

        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    private void handleEntitySpawn(int id) {
        final var entity = getEntityAsync(player.getLevel(), id);
        PlayerModel model = PlayerAnimator.api.getModelManager().getPlayerModel(entity);
        if(model == null)
            return;
        model.spawn(player.getBukkitEntity());
    }

    private void handleEntityDespawn(int id) {
        final var entity = getEntityAsync(player.getLevel(), id);
        PlayerModel model = PlayerAnimator.api.getModelManager().getPlayerModel(entity);
        if(model == null)
            return;
        model.despawn(player.getBukkitEntity());
    }

    @SuppressWarnings("unchecked")
    private Entity getEntityAsync(ServerLevel world, int id) {
        try {
            final var entityManager = (PersistentEntitySectionManager<net.minecraft.world.entity.Entity>) FieldUtils.getField(ServerLevel.class, "G").get(world);
            final var entity = entityManager.getEntityGetter().get(id);
            return entity == null ? null : entity.getBukkitEntity();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
