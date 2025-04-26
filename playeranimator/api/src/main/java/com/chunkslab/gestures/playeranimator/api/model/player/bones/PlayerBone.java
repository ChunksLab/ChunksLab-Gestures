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

package com.chunkslab.gestures.playeranimator.api.model.player.bones;

import com.chunkslab.gestures.playeranimator.api.animation.pack.Bone;
import com.chunkslab.gestures.playeranimator.api.model.player.LimbType;
import com.chunkslab.gestures.playeranimator.api.model.player.PlayerModel;
import com.chunkslab.gestures.playeranimator.api.utils.math.Offset;
import com.chunkslab.gestures.playeranimator.api.utils.math.TMath;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class PlayerBone {

	protected final Map<String, PlayerBone> children = new HashMap<>();
	@Getter protected final PlayerModel model;
	@Getter protected final Bone bone;
	@Getter protected final LimbType type;

	@Getter @Setter
	protected PlayerBone parent;

	// Properties
	protected Vector position = new Vector();
	protected Vector scale = new Vector();
	@Getter protected EulerAngle rotation = EulerAngle.ZERO;

	public PlayerBone(PlayerModel model, Bone bone) {
		this(model, bone, null);
	}

	public PlayerBone(PlayerModel model, Bone bone, LimbType type) {
		this.model = model;
		this.bone = bone;
		this.type = type;
	}

	public void update() {
		Vector fPosition = model.getAnimationProperty().getPositionFrame(bone.getName());
		EulerAngle fRotation = model.getAnimationProperty().getRotationFrame(bone.getName());
		Vector fScale = this.model.getAnimationProperty().getScaleFrame(bone.getName());

		Vector pPosition = parent == null ? getModel().getBaseVector() : parent.getPosition();
		EulerAngle pRotation = parent == null ? EulerAngle.ZERO : parent.getRotation();

		fPosition = Offset.getRelativeLocation(pRotation, fPosition.add(getBone().getOrigin()));
		fRotation = TMath.globalRotate(getBone().getRotation(), fRotation);

		if(parent == null) {
			fPosition.add(LimbType.base);
		}

		fPosition = Offset.rotateYaw(fPosition, Math.toRadians(getModel().getBaseYaw()));

		position = pPosition.add(fPosition);
		rotation = TMath.localRotate(pRotation, fRotation);
		scale = fScale;

		for(PlayerBone bone : children.values())
			bone.update();

	}

	public void addChild(PlayerBone bone) {
		bone.setParent(this);
		children.put(bone.getBone().getName(), bone);
	}

	public Vector getPosition() {
		return position.clone();
	}

	public boolean isInvisible() {
		return this.scale != null && this.scale.getX() < 1.0;
	}

}
