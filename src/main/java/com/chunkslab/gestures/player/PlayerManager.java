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

package com.chunkslab.gestures.player;

import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.player.IPlayerManager;
import org.bukkit.OfflinePlayer;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager implements IPlayerManager {

    private final Map<UUID, GesturePlayer> playerMap = new ConcurrentHashMap<>();

    @Override
    public Collection<GesturePlayer> getPlayers() {
        return playerMap.values();
    }

    @Override
    public void addPlayer(GesturePlayer player) {
        playerMap.put(player.getUniqueId(), player);
    }

    @Override
    public void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    @Override
    public void removePlayer(GesturePlayer player) {
        removePlayer(player.getUniqueId());
    }

    @Override
    public void removePlayer(OfflinePlayer offlinePlayer) {
        removePlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public GesturePlayer getPlayer(UUID uuid) {
        return playerMap.get(uuid);
    }

    @Override
    public GesturePlayer getPlayer(OfflinePlayer offlinePlayer) {
        return getPlayer(offlinePlayer.getUniqueId());
    }

    @Override
    public GesturePlayer getPlayer(String name) {
        return this.playerMap.values().stream().filter(player -> player.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

}