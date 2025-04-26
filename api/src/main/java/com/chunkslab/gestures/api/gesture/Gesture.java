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

package com.chunkslab.gestures.api.gesture;

import lombok.Getter;
import net.kyori.adventure.text.Component;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Gesture {
    private final String id;
    private final Component name;
    private final String permission;
    private final String font;
    private final Map<String, String> animation;
    private final boolean movement;
    private final double moveSpeed;
    private final GestureEquip gestureEquip;

    public Gesture(String id, Component name, String permission, String font, String animationStart, String animationIdle, String animationEnd, boolean movement, double moveSpeed, GestureEquip gestureEquip) {
        this.id = id;
        this.name = name;
        this.permission = permission;
        this.font = font;
        this.animation = new HashMap<>();
        this.movement = movement;
        this.moveSpeed = moveSpeed;
        this.gestureEquip = gestureEquip;
        if (animationStart != null) {
            this.animation.put("start", animationStart);
        }
        if (animationIdle != null) {
            this.animation.put("idle", animationIdle);
        }
        if (animationEnd != null) {
            this.animation.put("end", animationEnd);
        }
    }

    public String getFirstKeyAnimation() {
        if (this.animation.containsKey("start")) {
            return "start";
        }
        if (this.animation.containsKey("idle")) {
            return "idle";
        }
        if (this.animation.containsKey("end")) {
            return "end";
        }
        return "";
    }

    public String getAnimationStart() {
        return this.animation.get("start");
    }

    public String getAnimationIdle() {
        return this.animation.get("idle");
    }

    public String getAnimationEnd() {
        return this.animation.get("end");
    }
}