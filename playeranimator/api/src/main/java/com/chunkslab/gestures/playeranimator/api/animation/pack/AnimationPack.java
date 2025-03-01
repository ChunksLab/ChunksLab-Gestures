package com.chunkslab.gestures.playeranimator.api.animation.pack;

import com.chunkslab.gestures.playeranimator.api.animation.animation.Animation;
import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnimationPack {

	@Getter private final Map<String, Animation> animations = new HashMap<>();
	@Getter private final Set<Bone> bones = new HashSet<>();

	public void registerAnimation(Animation animation) {
		animations.put(animation.getName(), animation);
	}

	public Animation getAnimation(String name) {
		return animations.get(name);
	}

}
