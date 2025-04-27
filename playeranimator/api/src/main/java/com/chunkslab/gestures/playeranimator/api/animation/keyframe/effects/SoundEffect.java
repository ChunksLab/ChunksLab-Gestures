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

public class SoundEffect {
    private final String effect;
    private final String namespace;
    private final float volume;
    private final float pitch;

    public SoundEffect(String effect) {
        this(effect, 30.0f, 1.0f);
    }

    public SoundEffect(String effect, float volume, float pitch) {
        effect = effect.replace("\n", "");
        if (effect.contains(":")) {
            String[] sound = effect.split(":");
            this.namespace = sound[0];
            this.effect = sound[1];
            this.volume = volume;
            this.pitch = pitch;
            return;
        }
        this.namespace = "minecraft";
        this.effect = effect;
        this.volume = volume;
        this.pitch = pitch;
    }

    public String getEffect() {
        return this.effect;
    }

    public float getVolume() {
        return this.volume;
    }

    public float getPitch() {
        return this.pitch;
    }

    public String getNamespace() {
        return this.namespace;
    }
}