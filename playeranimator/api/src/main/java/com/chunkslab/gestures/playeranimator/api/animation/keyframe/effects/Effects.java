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

package com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects;

public class Effects {
    private ParticleEffect particleEffect;
    private SoundEffect sound;
    private String instructions;

    public Effects() {
        this(null, null, "");
    }

    public Effects(ParticleEffect particleEffect, SoundEffect sound, String instructions) {
        this.particleEffect = particleEffect;
        this.sound = sound;
        this.instructions = instructions;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public SoundEffect getSound() {
        return this.sound;
    }

    public ParticleEffect getParticle() {
        return this.particleEffect;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setSound(SoundEffect sound) {
        this.sound = sound;
    }

    public void setParticle(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
    }

    public String toString() {
        return "Effects{particle='" + String.valueOf(this.particleEffect) + "', sound='" + String.valueOf(this.sound) + "', instructions='" + this.instructions + "'}";
    }
}

