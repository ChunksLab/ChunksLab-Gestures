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

package com.chunkslab.gestures.playeranimator.api.animation.pack;

import com.chunkslab.gestures.playeranimator.api.animation.animation.Animation;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnimationPack {

	@Getter private final Map<String, Animation> animations = new HashMap<>();
	@Getter private final Set<Bone> bones = new HashSet<>();

	public void registerAnimation(Animation animation) {
		animations.put(animation.getName(), animation);
	}

	public Animation getAnimation(String name) {
		return animations.get(name);
	}

}
