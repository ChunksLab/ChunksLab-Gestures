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

package com.chunkslab.gestures.api.player;

import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface GesturePlayer {

    UUID getUniqueId();

    String getName();

    void setName(String name);

    String getSkinName();

    void setSkinName(String skinName);

    boolean isSkinStatus();

    void setSkinStatus(boolean skinStatus);

    default boolean inWardrobe() {
        return getWardrobe() != null;
    }

    Wardrobe getWardrobe();

    void setWardrobe(Wardrobe wardrobe);

    default boolean inGesture() {
        return getGesture() != null;
    }

    Gesture getGesture();

    void setGesture(Gesture gesture);

    FavoriteGestures getFavoriteGestures();

    void setFavoriteGestures(FavoriteGestures favoriteGestures);

    List<Gesture> getFavoriteGesturesList();

    Map<String, TextureWrapper> getTextures();

    void setTextures(Map<String, TextureWrapper> textures);

    @Nullable
    default Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    @NotNull
    default OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

}
