package com.chunkslab.gestures.playeranimator.api.model.player;

import com.chunkslab.gestures.playeranimator.api.animation.pack.Bone;
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
