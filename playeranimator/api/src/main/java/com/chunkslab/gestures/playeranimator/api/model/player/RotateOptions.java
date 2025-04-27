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

package com.chunkslab.gestures.playeranimator.api.model.player;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RotateOptions {
    private boolean rotate;
    private float finalYaw;
    private float currentYaw;
    private float modelRotation = 1.0f;

    public RotateOptions(boolean rotate) {
        this(rotate, -1.0f, Float.NaN);
    }

    public RotateOptions(boolean rotate, float finalYaw, float currentYaw) {
        this.rotate = rotate;
        this.finalYaw = finalYaw;
        this.currentYaw = currentYaw;
    }

    public void rotateYaw() {
        float difference = this.finalYaw - this.currentYaw;
        if (difference > 180.0f) {
            difference -= 360.0f;
        } else if (difference < -180.0f) {
            difference += 360.0f;
        }
        float interpolationFactor = 0.05f;
        float rotationAmount = difference * interpolationFactor;
        rotationAmount = Math.min(rotationAmount, this.modelRotation);
        rotationAmount = Math.max(rotationAmount, -this.modelRotation);
        this.currentYaw += rotationAmount;
        if ((double)Math.abs(difference) < 1.0) {
            this.currentYaw = this.finalYaw;
        }
    }

}
