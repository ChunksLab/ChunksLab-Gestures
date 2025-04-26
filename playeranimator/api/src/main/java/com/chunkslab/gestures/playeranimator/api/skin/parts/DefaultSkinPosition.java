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

import com.chunkslab.gestures.playeranimator.api.model.player.LimbType;
import com.chunkslab.gestures.playeranimator.api.skin.parts.positions.SkinPartPosition;
import com.chunkslab.gestures.playeranimator.api.skin.parts.positions.SkinPosition;
import com.chunkslab.gestures.playeranimator.api.skin.parts.positions.PartPosition;
import com.chunkslab.gestures.playeranimator.api.skin.parts.positions.SkinOverlayPartPosition;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

public enum DefaultSkinPosition implements SkinPosition {

    BASE(LimbType.HEAD,
            new SkinPartPosition(SkinPart.ARM_LEFT_BOTTOM, 0, 0)
    ),
    BODY_1(LimbType.CHEST,
            new SkinPartPosition(SkinPart.BODY_FRONT, 8, 8),
            new SkinPartPosition(SkinPart.BODY_BACK, 24, 8),
            new SkinPartPosition(SkinPart.BODY_TOP, 8, 0),
            new SkinPartPosition(SkinPart.BODY_RIGHT, 0, 8),
            new SkinPartPosition(SkinPart.BODY_LEFT, 16, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_FRONT,SkinPartOverlay.BODY_FRONT, 40, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_BACK, SkinPartOverlay.BODY_BACK, 56, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_TOP, SkinPartOverlay.BODY_TOP, 40, 0),
            new SkinOverlayPartPosition(SkinPart.BODY_RIGHT, SkinPartOverlay.BODY_RIGHT, 32, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_LEFT, SkinPartOverlay.BODY_LEFT, 48, 8)
    ),
    BODY_2(LimbType.HIP,
            new SkinPartPosition(SkinPart.BODY_FRONT_2, 8, 8),
            new SkinPartPosition(SkinPart.BODY_BACK_2, 24, 8),
            new SkinPartPosition(SkinPart.BODY_BOTTOM_2, 16, 0),
            new SkinPartPosition(SkinPart.BODY_RIGHT_2, 0, 8),
            new SkinPartPosition(SkinPart.BODY_LEFT_2, 16, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_FRONT_2, SkinPartOverlay.BODY_FRONT_2, 40, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_BACK_2, SkinPartOverlay.BODY_BACK_2, 56, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_BOTTOM_2, SkinPartOverlay.BODY_BOTTOM_2, 48, 0),
            new SkinOverlayPartPosition(SkinPart.BODY_RIGHT_2, SkinPartOverlay.BODY_RIGHT_2, 32, 8),
            new SkinOverlayPartPosition(SkinPart.BODY_LEFT_2, SkinPartOverlay.BODY_LEFT_2, 48, 8)
    ),
    ARM_RIGHT_1(LimbType.RIGHT_ARM,
            new SkinPartPosition(SkinPart.ARM_RIGHT_FRONT, 8, 8),
            new SkinPartPosition(SkinPart.ARM_RIGHT_BACK, 24, 8),
            new SkinPartPosition(SkinPart.ARM_RIGHT_TOP, 8, 0),
            new SkinPartPosition(SkinPart.ARM_RIGHT_OUTSIDE, 0, 8),
            new SkinPartPosition(SkinPart.ARM_RIGHT_INSIDE, 16, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_FRONT, SkinPartOverlay.ARM_RIGHT_FRONT, 40, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_BACK, SkinPartOverlay.ARM_RIGHT_BACK, 56, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_TOP, SkinPartOverlay.ARM_RIGHT_TOP, 40, 0),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_OUTSIDE, SkinPartOverlay.ARM_RIGHT_OUTSIDE, 32, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_INSIDE, SkinPartOverlay.ARM_RIGHT_INSIDE, 48, 8)
    ),
    ARM_RIGHT_2(LimbType.RIGHT_FOREARM,
            new SkinPartPosition(SkinPart.ARM_RIGHT_FRONT_2, 8, 8),
            new SkinPartPosition(SkinPart.ARM_RIGHT_BACK_2, 24, 8),
            new SkinPartPosition(SkinPart.ARM_RIGHT_BOTTOM_2, 16, 0),
            new SkinPartPosition(SkinPart.ARM_RIGHT_OUTSIDE_2, 0, 8),
            new SkinPartPosition(SkinPart.ARM_RIGHT_INSIDE_2, 16, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_FRONT_2, SkinPartOverlay.ARM_RIGHT_FRONT_2, 40, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_BACK_2, SkinPartOverlay.ARM_RIGHT_BACK_2, 56, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_BOTTOM_2, SkinPartOverlay.ARM_RIGHT_BOTTOM_2, 48, 0),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_OUTSIDE_2, SkinPartOverlay.ARM_RIGHT_OUTSIDE_2, 32, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_RIGHT_INSIDE_2, SkinPartOverlay.ARM_RIGHT_INSIDE_2, 48, 8)
    ),
    ARM_LEFT_1(LimbType.LEFT_ARM,
            new SkinPartPosition(SkinPart.ARM_LEFT_FRONT, 8, 8),
            new SkinPartPosition(SkinPart.ARM_LEFT_BACK, 24, 8),
            new SkinPartPosition(SkinPart.ARM_LEFT_TOP, 8, 0),
            new SkinPartPosition(SkinPart.ARM_LEFT_OUTSIDE, 0, 8),
            new SkinPartPosition(SkinPart.ARM_LEFT_INSIDE, 16, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_FRONT, SkinPartOverlay.ARM_LEFT_FRONT, 40, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_BACK, SkinPartOverlay.ARM_LEFT_BACK, 56, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_TOP, SkinPartOverlay.ARM_LEFT_TOP, 40, 0),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_OUTSIDE, SkinPartOverlay.ARM_LEFT_OUTSIDE, 32, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_INSIDE, SkinPartOverlay.ARM_LEFT_INSIDE, 48, 8)
    ),
    ARM_LEFT_2(LimbType.LEFT_FOREARM,
            new SkinPartPosition(SkinPart.ARM_LEFT_FRONT_2, 8, 8),
            new SkinPartPosition(SkinPart.ARM_LEFT_BACK_2, 24, 8),
            new SkinPartPosition(SkinPart.ARM_LEFT_BOTTOM_2, 16, 0),
            new SkinPartPosition(SkinPart.ARM_LEFT_OUTSIDE_2, 0, 8),
            new SkinPartPosition(SkinPart.ARM_LEFT_INSIDE_2, 16, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_FRONT_2, SkinPartOverlay.ARM_LEFT_FRONT_2, 40, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_BACK_2, SkinPartOverlay.ARM_LEFT_BACK_2, 56, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_BOTTOM_2, SkinPartOverlay.ARM_LEFT_BOTTOM_2, 48, 0),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_OUTSIDE_2, SkinPartOverlay.ARM_LEFT_OUTSIDE_2, 32, 8),
            new SkinOverlayPartPosition(SkinPart.ARM_LEFT_INSIDE_2, SkinPartOverlay.ARM_LEFT_INSIDE_2, 48, 8)
    ),
    LEG_RIGHT_1(LimbType.RIGHT_LEG,
            new SkinPartPosition(SkinPart.LEG_RIGHT_FRONT, 8, 8),
            new SkinPartPosition(SkinPart.LEG_RIGHT_BACK, 24, 8),
            new SkinPartPosition(SkinPart.LEG_RIGHT_TOP, 8, 0),
            new SkinPartPosition(SkinPart.LEG_RIGHT_OUTSIDE, 0, 8),
            new SkinPartPosition(SkinPart.LEG_RIGHT_INSIDE, 16, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_FRONT, SkinPartOverlay.LEG_RIGHT_FRONT, 40, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_BACK, SkinPartOverlay.LEG_RIGHT_BACK, 56, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_TOP, SkinPartOverlay.LEG_RIGHT_TOP, 40, 0),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_OUTSIDE, SkinPartOverlay.LEG_RIGHT_OUTSIDE, 32, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_INSIDE, SkinPartOverlay.LEG_RIGHT_INSIDE, 48, 8)
    ),
    LEG_RIGHT_2(LimbType.RIGHT_FORELEG,
            new SkinPartPosition(SkinPart.LEG_RIGHT_FRONT_2, 8, 8),
            new SkinPartPosition(SkinPart.LEG_RIGHT_BACK_2, 24, 8),
            new SkinPartPosition(SkinPart.LEG_RIGHT_BOTTOM_2, 16, 0),
            new SkinPartPosition(SkinPart.LEG_RIGHT_OUTSIDE_2, 0, 8),
            new SkinPartPosition(SkinPart.LEG_RIGHT_INSIDE_2, 16, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_FRONT_2, SkinPartOverlay.LEG_RIGHT_FRONT_2, 40, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_BACK_2, SkinPartOverlay.LEG_RIGHT_BACK_2, 56, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_BOTTOM_2, SkinPartOverlay.LEG_RIGHT_BOTTOM_2, 48, 0),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_OUTSIDE_2, SkinPartOverlay.LEG_RIGHT_OUTSIDE_2, 32, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_RIGHT_INSIDE_2, SkinPartOverlay.LEG_RIGHT_INSIDE_2, 48, 8)
    ),
    LEG_LEFT_1(LimbType.LEFT_LEG,
            new SkinPartPosition(SkinPart.LEG_LEFT_FRONT, 8, 8),
            new SkinPartPosition(SkinPart.LEG_LEFT_BACK, 24, 8),
            new SkinPartPosition(SkinPart.LEG_LEFT_TOP, 8, 0),
            new SkinPartPosition(SkinPart.LEG_LEFT_OUTSIDE, 0, 8),
            new SkinPartPosition(SkinPart.LEG_LEFT_INSIDE, 16, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_FRONT, SkinPartOverlay.LEG_LEFT_FRONT, 40, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_BACK, SkinPartOverlay.LEG_LEFT_BACK, 56, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_TOP, SkinPartOverlay.LEG_LEFT_TOP, 40, 0),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_OUTSIDE, SkinPartOverlay.LEG_LEFT_OUTSIDE, 32, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_INSIDE, SkinPartOverlay.LEG_LEFT_INSIDE, 48, 8)
    ),
    LEG_LEFT_2(LimbType.LEFT_FORELEG,
            new SkinPartPosition(SkinPart.LEG_LEFT_FRONT_2, 8, 8),
            new SkinPartPosition(SkinPart.LEG_LEFT_BACK_2, 24, 8),
            new SkinPartPosition(SkinPart.LEG_LEFT_BOTTOM_2, 16, 0),
            new SkinPartPosition(SkinPart.LEG_LEFT_OUTSIDE_2, 0, 8),
            new SkinPartPosition(SkinPart.LEG_LEFT_INSIDE_2, 16, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_FRONT_2, SkinPartOverlay.LEG_LEFT_FRONT_2, 40, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_BACK_2, SkinPartOverlay.LEG_LEFT_BACK_2, 56, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_BOTTOM_2, SkinPartOverlay.LEG_LEFT_BOTTOM_2, 48, 0),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_OUTSIDE_2, SkinPartOverlay.LEG_LEFT_OUTSIDE_2, 32, 8),
            new SkinOverlayPartPosition(SkinPart.LEG_LEFT_INSIDE_2, SkinPartOverlay.LEG_LEFT_INSIDE_2, 48, 8)
    );

    private final List<PartPosition> partPositions;
    @Getter
    private final LimbType limbType;
    private final int imageWidth;
    private final int imageHeight;
    private final int slimImageWidth;
    private final int slimImageHeight;

    DefaultSkinPosition(LimbType limbType, PartPosition ... partPositions) {
        this.partPositions = Arrays.asList(partPositions);
        this.limbType = limbType;
        this.imageWidth = SkinPosition.super.getImageWidth(false);
        this.imageHeight = SkinPosition.super.getImageHeight(false);
        this.slimImageWidth = SkinPosition.super.getImageWidth(true);
        this.slimImageHeight = SkinPosition.super.getImageHeight(true);
    }

    @Override
    public int getImageWidth(boolean slim) {
        return slim ? this.slimImageWidth : this.imageWidth;
    }

    @Override
    public int getImageHeight(boolean slim) {
        return slim ? this.slimImageHeight : this.imageHeight;
    }

    @Override
    public List<PartPosition> getPartPositions() {
        return this.partPositions;
    }
}