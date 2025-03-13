package com.chunkslab.gestures.playeranimator.api.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.util.Vector;

import java.util.Locale;

@AllArgsConstructor
public enum LimbType {

	HEAD(1, 1, false),
	HIP(8, 8, false),
	WAIST(8, 8, false),
	CHEST(8, 8, false),
	RIGHT_ARM(2, 5, false),
	RIGHT_FOREARM(4, 7, false),
	LEFT_ARM(3, 6, false),
	LEFT_FOREARM(4, 7, false),
	RIGHT_LEG(9, 9, false),
	RIGHT_FORELEG(9, 9, false),
	LEFT_LEG(9, 9, false),
	LEFT_FORELEG(9, 9, false),
	RIGHT_ITEM(-1, -1, true),
	LEFT_ITEM(-1, -1, true),
	INVISIBLE_HEAD(29, 29, false);

	public static final Vector base = new Vector(0.313, -1.85204000149011612, 0);

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
