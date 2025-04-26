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

package com.chunkslab.gestures.playeranimator.api.skin.parts;

import com.chunkslab.gestures.playeranimator.api.skin.images.ImageArea;
import lombok.Getter;

@Getter
public enum SlimSkinPart {
    ARM_RIGHT_TOP(44, 16, 3, 4),
    ARM_RIGHT_BOTTOM(47, 16, 3, 4),
    ARM_RIGHT_FRONT(44, 20, 3, 8),
    ARM_RIGHT_BACK(51, 20, 3, 8),
    ARM_RIGHT_TOP_2(44, 16, 3, 4),
    ARM_RIGHT_BOTTOM_2(47, 16, 3, 4),
    ARM_RIGHT_FRONT_2(44, 28, 3, 4),
    ARM_RIGHT_BACK_2(51, 28, 3, 4),
    ARM_LEFT_TOP(36, 48, 3, 4),
    ARM_LEFT_BOTTOM(39, 48, 3, 4),
    ARM_LEFT_FRONT(36, 52, 3, 8),
    ARM_LEFT_BACK(43, 52, 3, 8),
    ARM_LEFT_TOP_2(36, 48, 3, 4),
    ARM_LEFT_BOTTOM_2(39, 48, 3, 4),
    ARM_LEFT_FRONT_2(36, 60, 3, 4),
    ARM_LEFT_BACK_2(43, 60, 3, 4);

    private final ImageArea area;
    private final ImageArea overlayArea;

    SlimSkinPart(int x, int y, int w, int h) {
        this.area = new ImageArea(x, y, w, h);
        SlimSkinPartOverlay overlay = SlimSkinPartOverlay.valueOf(this.name());
        this.overlayArea = new ImageArea(overlay.getX(), overlay.getY(), w, h);
    }

}