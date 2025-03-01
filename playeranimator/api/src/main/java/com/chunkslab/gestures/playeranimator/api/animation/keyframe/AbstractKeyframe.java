package com.chunkslab.gestures.playeranimator.api.animation.keyframe;

import lombok.Getter;
import lombok.Setter;

public abstract class AbstractKeyframe<T> {

	@Getter @Setter private T value;
	@Getter @Setter private KeyframeType type;

	public AbstractKeyframe(T value) {
		this(value, KeyframeType.LINEAR);
	}

	public AbstractKeyframe(T value, KeyframeType type) {
		this.value = value;
		this.type = type;
	}

}
