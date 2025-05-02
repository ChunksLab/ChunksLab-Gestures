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

import com.chunkslab.gestures.hook.manager.GlowManager;
import me.eplugins.eglow.api.EGlowAPI;
import me.eplugins.eglow.api.enums.EGlowColor;
import me.eplugins.eglow.data.EGlowPlayer;
import org.bukkit.entity.Player;

public class EGlow implements GlowManager {

    private final EGlowAPI api = me.eplugins.eglow.EGlow.getAPI();

    @Override
    public void hideGlow(Player player) {
        EGlowPlayer eglowPlayer = api.getEGlowPlayer(player);
        if (eglowPlayer == null) return;
        if (api.getGlowColor(eglowPlayer).equalsIgnoreCase("none")) return;
        api.disableGlow(eglowPlayer);
    }

    @Override
    public void showGlow(Player player) {
        EGlowPlayer eglowPlayer = api.getEGlowPlayer(player);
        if (eglowPlayer == null) return;
        if (api.getGlowColor(eglowPlayer).equalsIgnoreCase("none")) return;
        api.enableGlow(eglowPlayer, EGlowColor.valueOf(api.getGlowColor(eglowPlayer).toUpperCase()));
    }

    @Override
    public String getName() {
        return "EGlow";
    }
}
