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

package com.chunkslab.gestures.playeranimator.api.skin.parts.positions;

import com.chunkslab.gestures.playeranimator.api.skin.parts.SkinPart;
import com.chunkslab.gestures.playeranimator.api.skin.parts.SkinPartOverlay;
import lombok.Getter;

@Getter
public class SkinOverlayPartPosition extends PartPosition {
    private final SkinPartOverlay partOverlay;

    public SkinOverlayPartPosition(SkinPart part, SkinPartOverlay partOverlay, int offsetX, int offsetY) {
        super(part, offsetX, offsetY);
        this.partOverlay = partOverlay;
    }

    public SkinOverlayPartPosition(SkinPart part, SkinPartOverlay partOverlay, int offsetX, int offsetY, int slimOffsetX, int slimOffsetY) {
        super(part, offsetX, offsetY, slimOffsetX, slimOffsetY);
        this.partOverlay = partOverlay;
    }

}

