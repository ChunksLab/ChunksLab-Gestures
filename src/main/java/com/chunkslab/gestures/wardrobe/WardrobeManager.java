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

package com.chunkslab.gestures.wardrobe;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.util.LocationUtils;
import com.chunkslab.gestures.api.wardrobe.IWardrobeManager;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.nms.api.CameraNMS;
import com.chunkslab.gestures.nms.api.WardrobeNMS;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class WardrobeManager implements IWardrobeManager {

    private final GesturesPlugin plugin;

    private final Map<String, Wardrobe> wardrobeMap = new ConcurrentHashMap<>();

    @Override
    public void enable() {
        wardrobeMap.clear();

        for (String id : plugin.getWardrobesFile().getKeys(false)) {
            ConfigurationSection section = plugin.getWardrobesFile().getConfigurationSection(id);
            Location npcLocation = LocationUtils.getLocation(section.getString("npcLocation"));
            Location playerLocation = LocationUtils.getLocation(section.getString("playerLocation"));
            Location exitLocation = LocationUtils.getLocation(section.getString("exitLocation"));
            boolean status = section.getBoolean("status");
            if (npcLocation == null || playerLocation == null || exitLocation == null)
                status = false;
            wardrobeMap.put(id, new Wardrobe(id, npcLocation, playerLocation, exitLocation, status));
        }
    }

    @Override
    public void addWardrobe(Wardrobe wardrobe) {
        wardrobeMap.put(wardrobe.getId(), wardrobe);
        plugin.getWardrobesFile().set(wardrobe.getId() + ".npcLocation", LocationUtils.getLocation(wardrobe.getNpcLocation()));
        plugin.getWardrobesFile().set(wardrobe.getId() + ".playerLocation", LocationUtils.getLocation(wardrobe.getPlayerLocation()));
        plugin.getWardrobesFile().set(wardrobe.getId() + ".exitLocation", LocationUtils.getLocation(wardrobe.getExitLocation()));
        plugin.getWardrobesFile().set(wardrobe.getId() + ".status", wardrobe.isStatus());
        plugin.getWardrobesFile().save();
    }

    @Override
    public void addWithoutSave(Wardrobe wardrobe) {
        wardrobeMap.put(wardrobe.getId(), wardrobe);
    }

    @Override
    public void deleteWardrobe(String id) {
        wardrobeMap.remove(id);
        plugin.getWardrobesFile().set(id, null);
        plugin.getWardrobesFile().save();
    }

    @Override
    public void kickAllPlayer() {
        for (GesturePlayer player : plugin.getPlayerManager().getPlayers()) {
            if (!player.inWardrobe()) return;
            Wardrobe wardrobe = player.getWardrobe();
            WardrobeNMS wardrobeNMS = plugin.getGestureNMS().getWardrobeNMS();
            wardrobeNMS.destroy(player.getPlayer());
            CameraNMS cameraNMS = plugin.getGestureNMS().getCameraNMS();
            cameraNMS.title(player.getPlayer(), PlaceholderAPI.setPlaceholders(null, plugin.getPluginConfig().getSettings().getWardrobeScreen()));
            cameraNMS.destroy(player.getPlayer());
            player.getPlayer().teleport(wardrobe.getExitLocation());
            player.setWardrobe(null);
        }
    }

    @Override
    public Wardrobe getWardrobe(String id) {
        return wardrobeMap.get(id);
    }

    @Override
    public Collection<Wardrobe> getWardrobes() {
        return wardrobeMap.values();
    }
}
