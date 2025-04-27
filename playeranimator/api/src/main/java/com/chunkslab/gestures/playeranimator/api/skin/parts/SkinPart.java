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

package com.chunkslab.gestures.playeranimator.api.skin.parts;

import com.chunkslab.gestures.playeranimator.api.skin.images.ImageArea;
import lombok.Getter;

@Getter
public enum SkinPart {
    LEG_RIGHT_TOP(4, 16, 4, 4),
    LEG_RIGHT_BOTTOM(8, 16, 4, 4),
    LEG_RIGHT_OUTSIDE(0, 20, 4, 8),
    LEG_RIGHT_FRONT(4, 20, 4, 8),
    LEG_RIGHT_INSIDE(8, 20, 4, 8),
    LEG_RIGHT_BACK(12, 20, 4, 8),
    LEG_RIGHT_TOP_2(4, 16, 4, 4),
    LEG_RIGHT_BOTTOM_2(8, 16, 4, 4),
    LEG_RIGHT_OUTSIDE_2(0, 28, 4, 4),
    LEG_RIGHT_FRONT_2(4, 28, 4, 4),
    LEG_RIGHT_INSIDE_2(8, 28, 4, 4),
    LEG_RIGHT_BACK_2(12, 28, 4, 4),
    BODY_TOP(20, 16, 8, 4),
    BODY_BOTTOM(28, 16, 8, 4),
    BODY_RIGHT(16, 20, 4, 8),
    BODY_FRONT(20, 20, 8, 8),
    BODY_LEFT(28, 20, 4, 8),
    BODY_BACK(32, 20, 8, 8),
    BODY_TOP_2(20, 16, 8, 4),
    BODY_BOTTOM_2(28, 16, 8, 4),
    BODY_RIGHT_2(16, 28, 4, 4),
    BODY_FRONT_2(20, 28, 8, 4),
    BODY_LEFT_2(28, 28, 4, 4),
    BODY_BACK_2(32, 28, 8, 4),
    ARM_RIGHT_TOP(44, 16, 4, 4, SlimSkinPart.ARM_RIGHT_TOP),
    ARM_RIGHT_BOTTOM(48, 16, 4, 4, SlimSkinPart.ARM_RIGHT_BOTTOM),
    ARM_RIGHT_OUTSIDE(40, 20, 4, 8),
    ARM_RIGHT_FRONT(44, 20, 4, 8, SlimSkinPart.ARM_RIGHT_FRONT),
    ARM_RIGHT_INSIDE(48, 20, 4, 8),
    ARM_RIGHT_BACK(52, 20, 4, 8, SlimSkinPart.ARM_RIGHT_BACK),
    ARM_RIGHT_TOP_2(44, 16, 4, 4, SlimSkinPart.ARM_RIGHT_TOP_2),
    ARM_RIGHT_BOTTOM_2(48, 16, 4, 4, SlimSkinPart.ARM_RIGHT_BOTTOM_2),
    ARM_RIGHT_OUTSIDE_2(40, 28, 4, 4),
    ARM_RIGHT_FRONT_2(44, 28, 4, 4, SlimSkinPart.ARM_RIGHT_FRONT_2),
    ARM_RIGHT_INSIDE_2(48, 28, 4, 4),
    ARM_RIGHT_BACK_2(52, 28, 4, 4, SlimSkinPart.ARM_RIGHT_BACK_2),
    LEG_LEFT_TOP(20, 48, 4, 4, LEG_RIGHT_TOP),
    LEG_LEFT_BOTTOM(24, 48, 4, 4, LEG_RIGHT_BOTTOM),
    LEG_LEFT_OUTSIDE(16, 52, 4, 8, LEG_RIGHT_OUTSIDE),
    LEG_LEFT_FRONT(20, 52, 4, 8, LEG_RIGHT_FRONT),
    LEG_LEFT_INSIDE(24, 52, 4, 8, LEG_RIGHT_INSIDE),
    LEG_LEFT_BACK(28, 52, 4, 8, LEG_RIGHT_BACK),
    LEG_LEFT_TOP_2(20, 48, 4, 4, LEG_RIGHT_TOP_2),
    LEG_LEFT_BOTTOM_2(24, 48, 4, 4, LEG_RIGHT_BOTTOM_2),
    LEG_LEFT_OUTSIDE_2(16, 60, 4, 4, LEG_RIGHT_OUTSIDE_2),
    LEG_LEFT_FRONT_2(20, 60, 4, 4, LEG_RIGHT_FRONT_2),
    LEG_LEFT_INSIDE_2(24, 60, 4, 4, LEG_RIGHT_INSIDE_2),
    LEG_LEFT_BACK_2(28, 60, 4, 4, LEG_RIGHT_BACK_2),
    ARM_LEFT_TOP(36, 48, 4, 4, ARM_RIGHT_TOP, SlimSkinPart.ARM_LEFT_TOP),
    ARM_LEFT_BOTTOM(40, 48, 4, 4, ARM_RIGHT_BOTTOM, SlimSkinPart.ARM_LEFT_BOTTOM),
    ARM_LEFT_OUTSIDE(32, 52, 4, 8, ARM_RIGHT_OUTSIDE),
    ARM_LEFT_FRONT(36, 52, 4, 8, ARM_RIGHT_FRONT, SlimSkinPart.ARM_LEFT_FRONT),
    ARM_LEFT_INSIDE(40, 52, 4, 8, ARM_RIGHT_INSIDE),
    ARM_LEFT_BACK(44, 52, 4, 8, ARM_RIGHT_BACK, SlimSkinPart.ARM_LEFT_BACK),
    ARM_LEFT_TOP_2(36, 48, 4, 4, ARM_RIGHT_TOP, SlimSkinPart.ARM_LEFT_TOP_2),
    ARM_LEFT_BOTTOM_2(40, 48, 4, 4, ARM_RIGHT_BOTTOM, SlimSkinPart.ARM_LEFT_BOTTOM_2),
    ARM_LEFT_OUTSIDE_2(32, 60, 4, 4, ARM_RIGHT_OUTSIDE_2),
    ARM_LEFT_FRONT_2(36, 60, 4, 4, ARM_RIGHT_FRONT, SlimSkinPart.ARM_LEFT_FRONT_2),
    ARM_LEFT_INSIDE_2(40, 60, 4, 4, ARM_RIGHT_INSIDE_2),
    ARM_LEFT_BACK_2(44, 60, 4, 4, ARM_RIGHT_BACK, SlimSkinPart.ARM_LEFT_BACK_2);

    private final ImageArea area;
    private final ImageArea overlayArea;
    private final SkinPart smallSkinPart;
    private final SlimSkinPart slimSkinPart;

    SkinPart(int x, int y, int w, int h) {
        this(x, y, w, h, null, null);
    }

    SkinPart(int x, int y, int w, int h, SkinPart smallSkinPart) {
        this(x, y, w, h, smallSkinPart, null);
    }

    SkinPart(int x, int y, int w, int h, SlimSkinPart slimSkinPart) {
        this(x, y, w, h, null, slimSkinPart);
    }

    SkinPart(int x, int y, int w, int h, SkinPart smallSkinPart, SlimSkinPart slimSkinPart) {
        this.area = new ImageArea(x, y, w, h);
        SkinPartOverlay overlay = SkinPartOverlay.valueOf(this.name());
        this.overlayArea = new ImageArea(overlay.getX(), overlay.getY(), w, h);
        this.smallSkinPart = smallSkinPart;
        this.slimSkinPart = slimSkinPart;
        boolean largeSkinsOnly = y >= 32;
        if (largeSkinsOnly && smallSkinPart == null) {
            throw new IllegalArgumentException();
        }
    }

}