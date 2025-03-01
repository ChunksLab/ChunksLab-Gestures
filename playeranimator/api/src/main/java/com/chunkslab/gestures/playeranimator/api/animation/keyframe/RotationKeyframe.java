package com.chunkslab.gestures.playeranimator.api.animation.keyframe;

import org.bukkit.util.EulerAngle;

public class RotationKeyframe extends AbstractKeyframe<EulerAngle> {

	public RotationKeyframe(EulerAngle value) {
		super(value);
	}

	public RotationKeyframe(EulerAngle value, KeyframeType type) {
		super(value, type);
	}
}
