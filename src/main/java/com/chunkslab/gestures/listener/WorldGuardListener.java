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

package com.chunkslab.gestures.listener;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.event.GesturePlayAnimationEvent;
import com.chunkslab.gestures.api.event.GestureStopAnimationEvent;
import com.chunkslab.gestures.util.ChatUtils;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.internal.WGMetadata;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.StateFlag;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

@RequiredArgsConstructor
public class WorldGuardListener implements Listener {

    private final GesturesPlugin plugin;

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onSpawnHorses(EntitySpawnEvent event) {
        if (event.getEntityType() != EntityType.HORSE) return;
        Horse horse = (Horse) event.getEntity();
        Location worldEditLocation = BukkitAdapter.adapt(horse.getLocation());
        ApplicableRegionSet applicableRegionSet = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(worldEditLocation);
        StateFlag.State state = applicableRegionSet.queryState(null, plugin.getHookManager().getWorldGuardManager().getFlag());
        if (state == null || state == StateFlag.State.DENY) {
            event.setCancelled(true);
            horse.remove();
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onGestureStart(GesturePlayAnimationEvent event) {
        Location worldEditLocation = BukkitAdapter.adapt(event.getGesturePlayer().getPlayer().getLocation());
        ApplicableRegionSet applicableRegionSet = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(worldEditLocation);
        StateFlag.State state = applicableRegionSet.queryState(null, plugin.getHookManager().getWorldGuardManager().getFlag());
        if (state == null || state == StateFlag.State.DENY) {
            event.setCancelled(true);
            ChatUtils.sendMessage(event.getGesturePlayer().getPlayer(), ChatUtils.format(plugin.getPluginMessages().getWorldGuardRegion()));
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onGestureStop(GestureStopAnimationEvent event) {
        Location worldEditLocation = BukkitAdapter.adapt(event.getGesturePlayer().getPlayer().getLocation());
        ApplicableRegionSet applicableRegionSet = WorldGuard.getInstance().getPlatform().getRegionContainer().createQuery().getApplicableRegions(worldEditLocation);
        StateFlag.State state = applicableRegionSet.queryState(null, plugin.getHookManager().getWorldGuardManager().getFlag());
        if (state == null || state == StateFlag.State.DENY) {
            return;
        }
        long now = System.currentTimeMillis() + 10000L;
        WGMetadata.put(event.getGesturePlayer().getPlayer(), "worldguard.region.disembarkMessage", now);
    }
}