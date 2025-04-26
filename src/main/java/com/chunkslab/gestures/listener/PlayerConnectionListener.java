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

package com.chunkslab.gestures.listener;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RequiredArgsConstructor
public class PlayerConnectionListener implements Listener {

    private final GesturesPlugin plugin;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        plugin.getPlayerAnimator().injectPlayer(player);
        plugin.getScheduler().runTaskAsync(() -> {
            GesturePlayer gesturePlayer = plugin.getDatabase().loadPlayer(player.getUniqueId());
            gesturePlayer.setName(player.getName());
            plugin.getSkinTask().getSkinQueue().offer(gesturePlayer);
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        plugin.getScheduler().runTaskAsync(() -> {
            if (gesturePlayer != null) {
                plugin.getDatabase().savePlayer(gesturePlayer);
                plugin.getPlayerManager().removePlayer(player.getUniqueId());
            }
        });
        plugin.getGestureManager().stopGesture(gesturePlayer);
        plugin.getPlayerAnimator().removePlayer(player);
    }
}