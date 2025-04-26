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

package com.chunkslab.gestures.playeranimator.api.animation.time;

import com.chunkslab.gestures.playeranimator.api.animation.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class AnimationProperty {

	@Getter private final Animation animation;
	@Getter private double time = 0;
	@Getter @Setter private double speed;
	@Getter private boolean finish;

	public AnimationProperty(Animation animation) {
		this(animation, 1);
	}

	public AnimationProperty(Animation animation, double speed) {
		this.animation = animation;
		this.speed = speed;
	}

	public boolean updateTime() {
		finish = false;
		switch (animation.getLoopMode()) {
			case ONCE -> {
				if(time < animation.getLength()) {
					time = Math.min(time + (speed / 20), animation.getLength());
					return true;
				}
			}
			case HOLD -> {
				time = Math.min(time + (speed / 20), animation.getLength());
				return true;
			}
			case LOOP -> {
				double oldTime = time;
				time = (time + speed / 20.0) % animation.getLength();
				double difference = oldTime - time;
				if (difference > 0.1) {
					finish = true;
				}
				return true;
			}
		}
		finish = true;
		return false;
	}

	public Vector getPositionFrame(String bone) {
		return animation.getPosition(bone, time);
	}

	public Vector getScaleFrame(String bone) {
		return animation.getScale(bone, this.time);
	}

	public EulerAngle getRotationFrame(String bone) {
		return animation.getRotation(bone, time);
	}

}
