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
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class PlayerItemBone extends PlayerBone{

	private static final Vector left = new Vector(-0.616, 0, 0);

	private boolean isLeft = false;

	public PlayerItemBone(PlayerModel model, Bone bone, LimbType type) {
		super(model, bone, type);
		if(type == LimbType.LEFT_ITEM)
			isLeft = true;
	}

	public void update() {
		Vector fPosition = model.getAnimationProperty().getPositionFrame(bone.getName());
		EulerAngle fRotation = model.getAnimationProperty().getRotationFrame(bone.getName());

		Vector pPosition = parent == null ? getModel().getBaseVector() : parent.getPosition();
		EulerAngle pRotation = parent == null ? EulerAngle.ZERO : parent.getRotation();

		fPosition = Offset.getRelativeLocation(pRotation, fPosition.add(getBone().getOrigin()));
		fRotation = TMath.globalRotate(getBone().getRotation(), fRotation);

		if(parent == null) {
			fPosition.add(LimbType.base);
		}

		if(isLeft)
			fPosition.add(left);

		fPosition = Offset.rotateYaw(fPosition, Math.toRadians(getModel().getBase().getLocation().getYaw()));

		position = pPosition.add(fPosition);
		rotation = TMath.localRotate(pRotation, fRotation);

		for(PlayerBone bone : children.values())
			bone.update();
	}

}
