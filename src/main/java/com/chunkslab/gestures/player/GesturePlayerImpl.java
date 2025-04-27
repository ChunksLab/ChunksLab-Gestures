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

import com.chunkslab.gestures.api.gesture.Gesture;
import com.chunkslab.gestures.api.player.FavoriteGestures;
import com.chunkslab.gestures.api.player.GesturePlayer;
import com.chunkslab.gestures.api.wardrobe.Wardrobe;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
public class GesturePlayerImpl implements GesturePlayer {
    private final UUID uniqueId;
    private String name, skinName;
    private boolean skinStatus;
    private Wardrobe wardrobe;
    private Gesture gesture;
    private FavoriteGestures favoriteGestures;
    private Map<String, TextureWrapper> textures;

    @Override
    public List<Gesture> getFavoriteGesturesList() {
        List<Gesture> gestures = new ArrayList<>();
        if (favoriteGestures.getOne() != null) gestures.add(favoriteGestures.getOne());
        if (favoriteGestures.getTwo() != null) gestures.add(favoriteGestures.getTwo());
        if (favoriteGestures.getThree() != null) gestures.add(favoriteGestures.getThree());
        if (favoriteGestures.getFour() != null) gestures.add(favoriteGestures.getFour());
        if (favoriteGestures.getFive() != null) gestures.add(favoriteGestures.getFive());
        if (favoriteGestures.getSix() != null) gestures.add(favoriteGestures.getSix());
        return gestures;
    }
}