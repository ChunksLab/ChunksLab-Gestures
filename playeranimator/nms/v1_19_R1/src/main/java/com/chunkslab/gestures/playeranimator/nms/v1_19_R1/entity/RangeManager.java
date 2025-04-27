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

package com.chunkslab.gestures.playeranimator.nms.v1_19_R1.entity;

import com.chunkslab.gestures.playeranimator.api.nms.IRangeManager;
import com.chunkslab.gestures.playeranimator.api.utils.FieldUtils;
import net.minecraft.server.level.ChunkMap;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class RangeManager implements IRangeManager {

    private final ChunkMap.TrackedEntity tracked;

    public RangeManager(ChunkMap.TrackedEntity tracked) {
        this.tracked = tracked;
    }

    @Override
    public void addPlayer(Player player) {
        tracked.seenBy.add(((CraftPlayer) player).getHandle().connection);
    }

    @Override
    public void removePlayer(Player player) {
        tracked.seenBy.remove(((CraftPlayer) player).getHandle().connection);
    }

    @Override
    public void setRenderDistance(int radius) {
        try {
            FieldUtils.getField(ChunkMap.TrackedEntity.class, "d").setInt(tracked, radius);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Set<Player> getPlayerInRange() {
        Set<Player> list = new HashSet<>();
        tracked.seenBy.forEach(serverPlayerConnection -> list.add(serverPlayerConnection.getPlayer().getBukkitEntity()));
        return list;
    }
}
