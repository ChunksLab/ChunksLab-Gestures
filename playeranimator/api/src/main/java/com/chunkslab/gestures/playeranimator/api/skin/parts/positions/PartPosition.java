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

package com.chunkslab.gestures.playeranimator.api.skin.parts.positions;

import com.chunkslab.gestures.playeranimator.api.skin.images.ImageArea;
import com.chunkslab.gestures.playeranimator.api.skin.parts.SkinPart;
import lombok.Getter;

public abstract class PartPosition {
    @Getter
    private final SkinPart part;
    private final int offsetX;
    private final int offsetY;
    private final int slimOffsetX;
    private final int slimOffsetY;

    public PartPosition(SkinPart part, int offsetX, int offsetY) {
        this(part, offsetX, offsetY, offsetX, offsetY);
    }

    public PartPosition(SkinPart part, int offsetX, int offsetY, int slimOffsetX, int slimOffsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.slimOffsetX = slimOffsetX;
        this.slimOffsetY = slimOffsetY;
        this.part = part;
    }

    public int getOffsetX(boolean slim) {
        return slim ? this.slimOffsetX : this.offsetX;
    }

    public int getOffsetY(boolean slim) {
        return slim ? this.slimOffsetY : this.offsetY;
    }

    int getMaxX(boolean slim) {
        return this.getOffsetX(slim) + this.getImageArea(slim).getW();
    }

    int getMaxY(boolean slim) {
        return this.getOffsetY(slim) + this.getImageArea(slim).getH();
    }

    public ImageArea getImageArea(boolean slim) {
        return slim && this.part.getSlimSkinPart() != null ? this.part.getSlimSkinPart().getArea() : this.part.getArea();
    }

}