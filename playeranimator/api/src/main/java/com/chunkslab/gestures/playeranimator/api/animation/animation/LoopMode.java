package com.chunkslab.gestures.playeranimator.api.animation.animation;

import java.util.Locale;

public enum LoopMode {
	ONCE, HOLD, LOOP;

	public static LoopMode get(String mode) {
		try {
			return valueOf(mode.toUpperCase(Locale.ENGLISH));
		}catch (IllegalArgumentException ignored) {}

		return ONCE;
	}

}
