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

package com.chunkslab.gestures.playeranimator.api.animation.animation;

import com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects.Effects;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Animation {

	private final Map<String, Timeline> timelines = new HashMap<>();
	@Getter private final String name;

	@Getter @Setter private double length;
	@Getter @Setter private LoopMode loopMode;
	@Getter @Setter private boolean override;

	public Animation(String name) {
		this.name = name;
	}

	public Timeline getOrCreateTimeline(String bone) {
		if(timelines.containsKey(bone))
			return timelines.get(bone);
		Timeline timeline = new Timeline();
		timelines.put(bone, timeline);
		return timeline;
	}

	public Vector getPosition(String bone, double time) {
		if(!timelines.containsKey(bone))
			return new Vector();
		return timelines.get(bone).getPositionFrame(time);
	}

	public EulerAngle getRotation(String bone, double time) {
		if(!timelines.containsKey(bone))
			return EulerAngle.ZERO;
		return timelines.get(bone).getRotationFrame(time);
	}

	public Vector getScale(String bone, double time) {
		if (!timelines.containsKey(bone)) {
			return null;
		}
		return timelines.get(bone).getScaleFrame(time);
	}

	public Effects getEffects(String bone, double time) {
		if(!timelines.containsKey(bone))
			return null;
		return timelines.get(bone).getEffectsFrame(time);
	}

}
