package com.chunkslab.gestures.playeranimator.api.animation.animation;

import com.chunkslab.gestures.playeranimator.api.animation.keyframe.*;
import com.chunkslab.gestures.playeranimator.api.utils.math.TMath;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.TreeMap;

public class Timeline {

	private final TreeMap<Double, PositionKeyframe> position = new TreeMap<>();
	private final TreeMap<Double, ScaleKeyframe> scale = new TreeMap();
	private final TreeMap<Double, RotationKeyframe> rotation = new TreeMap<>();

	public void addPositionFrame(double frame, Vector vector, KeyframeType type) {
		position.put(frame, new PositionKeyframe(vector, type));
	}

	public void addRotationFrame(double frame, EulerAngle angle, KeyframeType type) {
		rotation.put(frame, new RotationKeyframe(angle, type));
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

	public Vector getScaleFrame(double time) {
		if(position.isEmpty())
			return null;
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
