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

package com.chunkslab.gestures.playeranimator.api.animation.animation;

import com.chunkslab.gestures.playeranimator.api.animation.keyframe.*;
import com.chunkslab.gestures.playeranimator.api.animation.keyframe.effects.Effects;
import com.chunkslab.gestures.playeranimator.api.utils.math.TMath;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.TreeMap;

public class Timeline {

	private final TreeMap<Double, PositionKeyframe> position = new TreeMap<>();
	private final TreeMap<Double, ScaleKeyframe> scale = new TreeMap();
	private final TreeMap<Double, RotationKeyframe> rotation = new TreeMap<>();
	private final TreeMap<Double, EffectsKeyframe> effect = new TreeMap<>();

	public void addPositionFrame(double frame, Vector vector, KeyframeType type) {
		position.put(frame, new PositionKeyframe(vector, type));
	}

	public void addRotationFrame(double frame, EulerAngle angle, KeyframeType type) {
		rotation.put(frame, new RotationKeyframe(angle, type));
	}

	public void addScaleFrame(double frame, Vector vector, KeyframeType type) {
		scale.put(frame, new ScaleKeyframe(vector, type));
	}

	public EffectsKeyframe addOrGetEffectFrame(double frame) {
		if (effect.containsKey(frame)) {
			return effect.get(frame);
		}
		EffectsKeyframe effectsKeyframe = new EffectsKeyframe(new Effects(), KeyframeType.LINEAR);
		effect.put(frame, effectsKeyframe);
		return effectsKeyframe;
	}

	public Effects getEffectsFrame(double time) {
		double timeThreshold = 1.5;
		if (effect.isEmpty()) {
			return null;
		}
		if (effect.containsKey(time)) {
			return effect.get(time).getValue();
		}
		Double lastTime = effect.floorKey(time);
		if (lastTime == null) {
			return null;
		}
		Double nextTime = effect.ceilingKey(time);
		if (nextTime == null) {
			return effect.get(lastTime).getValue();
		}
		double t = (time - lastTime) / (nextTime - lastTime);
		double realTime = TMath.lerp(lastTime, nextTime, t);
		if (nextTime.equals(lastTime)) {
			return effect.get(lastTime).getValue();
		}
		if (Math.abs(realTime - time) > timeThreshold) {
			return null;
		}
		return effect.get(lastTime).getValue();
	}

	public Vector getScaleFrame(double time) {
		if(scale.isEmpty())
			return null;
		if(scale.containsKey(time))
			return scale.get(time).getValue().clone();

		double nextTime = getHigherKey(scale, time);
		double lastTime = getLowerKey(scale, time);
		if(nextTime == lastTime)
			return scale.get(lastTime).getValue().clone();

		double t = (time - lastTime) / (nextTime - lastTime);

		ScaleKeyframe nextVector = scale.get(nextTime);
		ScaleKeyframe lastVector = scale.get(lastTime);

		switch (getType(lastVector, nextVector)) {
			case LINEAR -> {
				return TMath.lerp(lastVector.getValue(), nextVector.getValue(), t);
			}
			case SMOOTH -> {
				double nextControlTime = getHigherKey(scale, nextTime);
				double lastControlTime = getLowerKey(scale, lastTime);
				ScaleKeyframe nextControlVector = scale.get(nextControlTime);
				ScaleKeyframe lastControlVector = scale.get(lastControlTime);
				return TMath.smoothLerp(lastControlVector.getValue(), lastVector.getValue(), nextVector.getValue(), nextControlVector.getValue(), t);
			}
			case STEP -> {
				return lastVector.getValue().clone();
			}
		}

		return TMath.lerp(lastVector.getValue(), nextVector.getValue(), t);
	}

	public Vector getPositionFrame(double time) {
		if(position.isEmpty())
			return new Vector();
		if(position.containsKey(time))
			return position.get(time).getValue().clone();

		double nextTime = getHigherKey(position, time);
		double lastTime = getLowerKey(position, time);
		if(nextTime == lastTime)
			return position.get(lastTime).getValue().clone();

		double t = (time - lastTime) / (nextTime - lastTime);

		PositionKeyframe nextVector = position.get(nextTime);
		PositionKeyframe lastVector = position.get(lastTime);

		switch (getType(lastVector, nextVector)) {
			case LINEAR -> {
				return TMath.lerp(lastVector.getValue(), nextVector.getValue(), t);
			}
			case SMOOTH -> {
				double nextControlTime = getHigherKey(position, nextTime);
				double lastControlTime = getLowerKey(position, lastTime);
				PositionKeyframe nextControlVector = position.get(nextControlTime);
				PositionKeyframe lastControlVector = position.get(lastControlTime);
				return TMath.smoothLerp(lastControlVector.getValue(), lastVector.getValue(), nextVector.getValue(), nextControlVector.getValue(), t);
			}
			case STEP -> {
				return lastVector.getValue().clone();
			}
		}

		return TMath.lerp(lastVector.getValue(), nextVector.getValue(), t);
	}

	public EulerAngle getRotationFrame(double time) {
		if(rotation.isEmpty())
			return EulerAngle.ZERO;
		if(rotation.containsKey(time))
			return rotation.get(time).getValue();

		double nextTime = getHigherKey(rotation, time);
		double lastTime = getLowerKey(rotation, time);
		if(nextTime == lastTime)
			return rotation.get(lastTime).getValue();

		double t = (time - lastTime) / (nextTime - lastTime);

		RotationKeyframe nextAngle = rotation.get(nextTime);
		RotationKeyframe lastAngle = rotation.get(lastTime);

		switch (getType(lastAngle, nextAngle)) {
			case LINEAR -> {
				return TMath.lerp(lastAngle.getValue(), nextAngle.getValue(), t);
			}
			case SMOOTH -> {
				double nextControlTime = getHigherKey(rotation, nextTime);
				double lastControlTime = getLowerKey(rotation, lastTime);
				RotationKeyframe nextControlAngle = rotation.get(nextControlTime);
				RotationKeyframe lastControlAngle = rotation.get(lastControlTime);
				return TMath.smoothLerp(lastControlAngle.getValue(), lastAngle.getValue(), nextAngle.getValue(), nextControlAngle.getValue(), t);
			}
			case STEP -> {
				return lastAngle.getValue();
			}
		}

		return TMath.lerp(lastAngle.getValue(), nextAngle.getValue(), t);
	}

	private double getHigherKey(TreeMap<Double, ?> map, double time) {
		Double high = map.higherKey(time);
		if(high == null)// No next frame
			return map.lastKey();
		return high;
	}

	private double getLowerKey(TreeMap<Double, ?> map, double time) {
		Double low = map.lowerKey(time);
		if(low == null)// No next frame
			return map.firstKey();
		return low;
	}
	private KeyframeType getType(AbstractKeyframe<?> last, AbstractKeyframe<?> next) {
		if(last.getType() == KeyframeType.STEP)
			return KeyframeType.STEP;

		if(last.getType() == KeyframeType.SMOOTH || next.getType() == KeyframeType.SMOOTH)
			return KeyframeType.SMOOTH;

		return KeyframeType.LINEAR;
	}

}
