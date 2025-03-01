package com.chunkslab.gestures.playeranimator.api.animation.pack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

@Getter @AllArgsConstructor
public class Bone {

	private final String name;
	private final Vector origin;
	private final EulerAngle rotation;
	private final Set<Bone> children = new HashSet<>();

}
