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

package com.chunkslab.gestures.hook;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.hook.impl.glow.EGlow;
import com.chunkslab.gestures.hook.impl.glow.FancyGlow;
import com.chunkslab.gestures.hook.manager.GlowManager;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bukkit.plugin.Plugin;

import java.util.Map;

@RequiredArgsConstructor
public class HookManager {

    private final Map<String, Class<? extends GlowManager>> glowHooks = Maps.newConcurrentMap();

    private final GesturesPlugin plugin;

    @Getter private GlowManager glowManager;

    public void enable() {
        glowHooks.put("EGlow", EGlow.class);
        glowHooks.put("FancyGlow", FancyGlow.class);

        initGlowManager();
    }

    @SneakyThrows
    private void initGlowManager() {
        for (Map.Entry<String, Class<? extends GlowManager>> entry : glowHooks.entrySet()) {
            Plugin p = plugin.getServer().getPluginManager().getPlugin(entry.getKey());
            if (p != null && p.isEnabled()) {
                glowManager = entry.getValue().getDeclaredConstructor(GesturesPlugin.class).newInstance(plugin);
                break;
            }
        }
    }
}