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

package com.chunkslab.gestures.hook.impl.glow;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.hook.manager.GlowManager;
import hhitt.fancyglow.api.FancyGlowAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FancyGlow implements GlowManager {

    private final FancyGlowAPI fancyGlowAPI = GesturesPlugin.getInstance().getServer().getServicesManager().load(FancyGlowAPI.class);

    @Override
    public void hideGlow(Player player) {
        if (fancyGlowAPI == null) return;
        if (!fancyGlowAPI.hasGlow(player)) return;
        fancyGlowAPI.removePlayerGlow(player);
    }

    @Override
    public void showGlow(Player player) {
        if (fancyGlowAPI == null) return;
        if (fancyGlowAPI.hasGlow(player)) return;
        fancyGlowAPI.setPlayerGlowColor(player, ChatColor.valueOf(fancyGlowAPI.getPlayerGlowColorName(player).toUpperCase()));
    }

    @Override
    public String getName() {
        return "FancyGlow";
    }
}
