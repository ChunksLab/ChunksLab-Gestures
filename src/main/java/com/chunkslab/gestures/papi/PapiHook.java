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

package com.chunkslab.gestures.papi;

import com.chunkslab.gestures.GesturesPlugin;
import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.util.ChatUtils;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class PapiHook extends PlaceholderExpansion {

    private final GesturesPlugin plugin;

    @Override
    public @NotNull String getIdentifier() {
        return "gestures";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        GesturePlayer gesturePlayer = plugin.getPlayerManager().getPlayer(player);
        params = params.toUpperCase();

        if (params.startsWith("FAVORITE_GESTURE_")) {
            try {
                int index = Integer.parseInt(params.substring("FAVORITE_GESTURE_".length())) - 1;
                if (index < 0 || index >= gesturePlayer.getFavoriteGesturesList().size()) {
                    return "None";
                }
                Gesture gesture = gesturePlayer.getFavoriteGesturesList().get(index);
                return gesture != null ? ChatUtils.LEGACY_AMPERSAND.serialize(gesture.getName()) : "None";
            } catch (NumberFormatException e) {
                return "Invalid Index";
            }
        }

        return switch (params) {
            case "IN_GESTURE" -> gesturePlayer.inGesture() ? "true" : "false";
            case "IN_GESTURE_NAME" -> gesturePlayer.inGesture() ? ChatUtils.LEGACY_AMPERSAND.serialize(gesturePlayer.getGesture().getName()) : "None";
            case "IN_WARDROBE" -> gesturePlayer.inWardrobe() ? "true" : "false";
            default -> "UNKNOWN PLACEHOLDER";
        };
    }
}