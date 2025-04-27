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

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.Locale;

@AllArgsConstructor
public enum LimbType {

	HEAD(1, 1, false),
	HIP(8, 8, false),
	CHEST(9, 9, false),
	RIGHT_ARM(2, 5, false),
	RIGHT_FOREARM(4, 7, false),
	LEFT_ARM(3, 6, false),
	LEFT_FOREARM(4, 7, false),
	RIGHT_LEG(10, 10, false),
	RIGHT_FORELEG(11, 11, false),
	LEFT_LEG(10, 10, false),
	LEFT_FORELEG(11, 11, false),
	RIGHT_ITEM(-1, -1, true),
	LEFT_ITEM(-1, -1, true),
	EFFECTS(-1, -1, false),
	PARTICLE(-1, -1, false),
	INVISIBLE_HEAD(12, 12, false);

	public static final Vector base = new Vector(0.313, -1.8520400014901162, 0.0);

	@Getter private final int modelId;
	@Getter private final int slimId;
	@Getter private final boolean isItem;

	public static LimbType get(String limb) {
		try {
			return valueOf(limb.toUpperCase(Locale.ENGLISH));
		}catch (IllegalArgumentException ignored) {}

		return null;
	}
}
