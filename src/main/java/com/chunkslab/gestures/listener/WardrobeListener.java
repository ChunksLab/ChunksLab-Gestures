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
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.gui.WardrobeGesturesGui;
import com.chunkslab.gestures.nms.api.CameraNMS;
import com.chunkslab.gestures.nms.api.WardrobeNMS;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

@RequiredArgsConstructor
public class WardrobeListener implements Listener {

    private final GesturesPlugin plugin;

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        if (!gesturePlayer.inWardrobe()) return;
        event.setCancelled(true);
        plugin.getGestureManager().stopGesture(gesturePlayer, true);
        Wardrobe wardrobe = gesturePlayer.getWardrobe();
        WardrobeNMS wardrobeNMS = plugin.getGestureNMS().getWardrobeNMS();
        wardrobeNMS.destroy(player);
        CameraNMS cameraNMS = plugin.getGestureNMS().getCameraNMS();
        cameraNMS.title(player, PlaceholderAPI.setPlaceholders(null, plugin.getPluginConfig().getSettings().getWardrobeScreen()));
        cameraNMS.destroy(player);
        player.teleport(wardrobe.getExitLocation());
        gesturePlayer.setWardrobe(null);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!event.getAction().isLeftClick()) return;
        Player player = event.getPlayer();
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        if (!gesturePlayer.inWardrobe()) return;
        event.setCancelled(true);
        WardrobeGesturesGui.open(gesturePlayer, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());
        if (!gesturePlayer.inWardrobe()) return;
        Wardrobe wardrobe = gesturePlayer.getWardrobe();
        WardrobeNMS wardrobeNMS = plugin.getGestureNMS().getWardrobeNMS();
        wardrobeNMS.destroy(player);
        CameraNMS cameraNMS = plugin.getGestureNMS().getCameraNMS();
        cameraNMS.title(player, PlaceholderAPI.setPlaceholders(null, plugin.getPluginConfig().getSettings().getWardrobeScreen()));
        cameraNMS.destroy(player);
        player.teleport(wardrobe.getExitLocation());
        gesturePlayer.setWardrobe(null);
    }
}