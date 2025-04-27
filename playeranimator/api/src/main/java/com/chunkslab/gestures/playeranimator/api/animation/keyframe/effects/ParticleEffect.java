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

package com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects;

public class ParticleEffect {
    private final String effect;
    private final float xDist;
    private final float yDist;
    private final float zDist;
    private final float maxSpeed;
    private final int count;
    private final boolean overrideLimiter;

    public ParticleEffect(String effect) {
        this(effect, 0.0f, 0.0f, 0.0f, 1.0f, 10);
    }

    public ParticleEffect(String effect, float xDist, float yDist, float zDist, float maxSpeed, int count) {
        this(effect, xDist, yDist, zDist, maxSpeed, count, true);
    }

    protected ParticleEffect(String effect, float xDist, float yDist, float zDist, float maxSpeed, int count, boolean overrideLimiter) {
        this.effect = effect;
        this.xDist = xDist;
        this.yDist = yDist;
        this.zDist = zDist;
        this.maxSpeed = maxSpeed;
        this.count = count;
        this.overrideLimiter = overrideLimiter;
    }

    public String getEffect() {
        return this.effect;
    }

    public float getXDist() {
        return this.xDist;
    }

    public float getYDist() {
        return this.yDist;
    }

    public float getZDist() {
        return this.zDist;
    }

    public float getMaxSpeed() {
        return this.maxSpeed;
    }

    public int getCount() {
        return this.count;
    }

    public boolean isOverrideLimiter() {
        return this.overrideLimiter;
    }
}