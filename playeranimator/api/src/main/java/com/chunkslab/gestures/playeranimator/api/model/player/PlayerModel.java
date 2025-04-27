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

package com.chunkslab.gestures.playeranimator.api.model.player;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.PlayerAnimatorPlugin;
import com.chunkslab.gestures.playeranimator.api.animation.animation.Animation;
import com.chunkslab.gestures.playeranimator.api.animation.pack.AnimationPack;
import com.chunkslab.gestures.playeranimator.api.animation.pack.Bone;
import com.chunkslab.gestures.playeranimator.api.animation.time.AnimationProperty;
import com.chunkslab.gestures.playeranimator.api.exceptions.MissingAnimationsException;
import com.chunkslab.gestures.playeranimator.api.exceptions.UnknownAnimationException;
import com.chunkslab.gestures.playeranimator.api.model.player.bones.PlayerBone;
import com.chunkslab.gestures.playeranimator.api.model.player.bones.PlayerEffectsBone;
import com.chunkslab.gestures.playeranimator.api.model.player.bones.PlayerItemBone;
import com.chunkslab.gestures.playeranimator.api.nms.IRangeManager;
import com.chunkslab.gestures.playeranimator.api.nms.IRenderer;
import com.chunkslab.gestures.playeranimator.api.texture.TextureWrapper;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

public class PlayerModel {

	protected final Map<LimbType, IRenderer> limbs = new HashMap<>();
	private final Map<LimbType, PlayerBone> bones = new HashMap<>();
	private final Map<String, PlayerBone> children = new HashMap<>();

	@Getter @Setter private Entity base;
	@Getter @Setter private Map<String, TextureWrapper> texture;
	@Getter private AnimationProperty animationProperty;
	@Getter private IRangeManager rangeManager;
	protected RotateOptions rotateOptions;
	private Location locationBuffer;

	public PlayerModel(Player player) {
		texture = new HashMap<>();
		base = player;
		String raw = PlayerAnimator.api.getNms().getTexture(player);
		texture.put("HEAD", TextureWrapper.fromBase64(raw));
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		rotateOptions = new RotateOptions(false);
		initialize();
	}

	public PlayerModel(Player player, Map<String, TextureWrapper> texture) {
		base = player;
		this.texture = texture;
		if (!this.texture.containsKey("HEAD")) {
			String raw = PlayerAnimator.api.getNms().getTexture(player);
			this.texture.put("HEAD", TextureWrapper.fromBase64(raw));
		}
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		rotateOptions = new RotateOptions(false);
		initialize();
	}

	public PlayerModel(Entity base, String url, boolean isSlim) {
		texture = new HashMap<>();
		this.base = base;
		texture.put("HEAD", new TextureWrapper(url, isSlim));
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		rotateOptions = new RotateOptions(false);
		initialize();
	}

	public PlayerModel(Entity base, Map<String, TextureWrapper> texture) {
		this.base = base;
		this.texture = texture;
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		rotateOptions = new RotateOptions(false);
		initialize();
	}

	public PlayerModel(Entity base, Player player, Map<String, TextureWrapper> texture) {
		this.base = base;
		this.texture = texture;
		if (!this.texture.containsKey("HEAD")) {
			String raw = PlayerAnimator.api.getNms().getTexture(player);
			this.texture.put("HEAD", TextureWrapper.fromBase64(raw));
		}
		rangeManager = PlayerAnimator.api.getNms().createRangeManager(base);
		initialize();
	}

	protected void initialize() {
		for(LimbType type : LimbType.values())
			limbs.put(type, PlayerAnimator.api.getNms().createRenderer());
	}

	public void spawn() {
		for(LimbType type : LimbType.values()) {
			if(!limbs.containsKey(type) || !bones.containsKey(type))
				continue;
			limbs.get(type).spawn();
		}
	}

	public void spawn(Player player) {
		for(LimbType type : LimbType.values()) {
			if(!limbs.containsKey(type) || !bones.containsKey(type))
				continue;
			limbs.get(type).spawn(player);
		}
	}

	public void despawn() {
		for(LimbType type : bones.keySet())
			limbs.get(type).despawn();
	}

	public void despawn(Player player) {
		for(LimbType type : bones.keySet())
			limbs.get(type).despawn(player);
	}

	protected void initializeAnimation() {}

	public void changeItem(Hand hand, int slot) {
		if (hand == Hand.ALL) {
			IRenderer renderer;
			if (limbs.containsKey(LimbType.RIGHT_ITEM)) {
				renderer = limbs.get(LimbType.RIGHT_ITEM);
				if (renderer == null) {
					return;
				}
				renderer.changeItem(hand, slot);
			}
			if (limbs.containsKey(LimbType.LEFT_ITEM)) {
				renderer = limbs.get(LimbType.RIGHT_ITEM);
				if (renderer == null) {
					return;
				}
				renderer.changeItem(hand, slot);
			}
			return;
		}
		if (hand == Hand.MAIN_HAND && limbs.containsKey(LimbType.RIGHT_ITEM)) {
			IRenderer renderer = limbs.get(LimbType.RIGHT_ITEM);
			if (renderer == null) {
				return;
			}
			renderer.changeItem(hand, slot);
			return;
		}
		if (!limbs.containsKey(LimbType.LEFT_ITEM)) {
			return;
		}
		IRenderer renderer = limbs.get(LimbType.LEFT_ITEM);
		if (renderer == null) {
			return;
		}
		renderer.changeItem(hand, slot);
	}

	public void playAnimation(String name) {
		String[] keys = name.split("\\.", 3);
		if(keys.length < 3)
			throw new IllegalArgumentException();
		final var animationPack = PlayerAnimator.api.getAnimationManager().getAnimationPack(keys[0] + ":" + keys[1]);
		if(animationPack == null) {
		    PlayerAnimatorPlugin.plugin.getLogger().log(Level.SEVERE, "AnimationPack " + keys[0] + ":" + keys[1] + " not found");
		    throw new MissingAnimationsException();
		}
		
		final var animation = animationPack.getAnimation(keys[2]);
		if(animation == null)
			throw new UnknownAnimationException();

		bones.clear();
		children.clear();
		for(Bone bone : animationPack.getBones())
			children.put(bone.getName(), setBones(bone));

		animationProperty = new AnimationProperty(animation);
		update(false);
		spawn();
		initializeAnimation();

		PlayerAnimator.api.getModelManager().registerModel(this);
	}

	public void nextAnimation(String name) {
		String[] keys = name.split("\\.", 3);
		if (keys.length < 3) {
			throw new IllegalArgumentException();
		}
		AnimationPack animationPack = PlayerAnimator.api.getAnimationManager().getAnimationPack(keys[0] + ":" + keys[1]);
		if (animationPack == null) {
			PlayerAnimatorPlugin.plugin.getLogger().log(Level.SEVERE, "AnimationPack " + keys[0] + ":" + keys[1] + " not found");
			throw new MissingAnimationsException();
		}
		Animation animation = animationPack.getAnimation(keys[2]);
		if (animation == null) {
			throw new UnknownAnimationException();
		}
		this.animationProperty = new AnimationProperty(animation);
		this.update(false);
	}

	private PlayerBone setBones(Bone bone) {
		LimbType type = LimbType.get(bone.getName().startsWith("particle") ? "PARTICLE" : bone.getName());
		PlayerBone playerBone;
		if(type != null) {
			playerBone = type.isItem() ? new PlayerItemBone(this, bone, type) : type.equals(LimbType.EFFECTS) || type.equals(LimbType.PARTICLE) ? new PlayerEffectsBone(this, bone, type.equals(LimbType.PARTICLE)) : new PlayerBone(this, bone, type);
			limbs.get(type).setLimb(playerBone);
			bones.put(type, playerBone);
		}else {
			playerBone = new PlayerBone(this, bone);
		}

		for(Bone child : bone.getChildren())
			playerBone.addChild(setBones(child));

		return playerBone;
	}

	public boolean update() {
		return update(true);
	}

	protected boolean update(boolean updateTime) {
		locationBuffer = getBase().getLocation();
		rotateOptions.setFinalYaw(locationBuffer.getYaw());
		for(PlayerBone bone : children.values())
			bone.update();
		for(LimbType type : bones.keySet())
			limbs.get(type).update();
		if(updateTime && !getAnimationProperty().updateTime())
			animationProperty = null;
		return animationProperty != null && !base.isDead();
	}

	protected boolean finishAnimation() {
		if (getAnimationProperty() == null) {
			return true;
		}
		return getAnimationProperty().isFinish();
	}

	public Set<Player> getSeenBy() {
		final var set = getRangeManager().getPlayerInRange();
		if(base instanceof Player player)
			set.add(player);
		return set;
	}

	public Vector getBaseVector() {
		if(locationBuffer != null)
			return locationBuffer.toVector();
		return getBase().getLocation().toVector();
	}

	public float getBaseYaw() {
		if (Float.isNaN(rotateOptions.getCurrentYaw()) && locationBuffer != null)
			return getDefaultYaw();
		if (!rotateOptions.isRotate())
			return rotateOptions.getCurrentYaw();
		return getBlockYaw();
	}

	public float getDefaultYaw() {
		rotateOptions.setCurrentYaw(locationBuffer.getYaw());
		return rotateOptions.getCurrentYaw();
	}

	public float getBlockYaw() {
		rotateOptions.rotateYaw();
		return rotateOptions.getCurrentYaw();
	}

}
