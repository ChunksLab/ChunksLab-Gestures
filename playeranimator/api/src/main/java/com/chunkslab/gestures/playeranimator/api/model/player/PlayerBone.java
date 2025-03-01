package com.chunkslab.gestures.playeranimator.api.model.player;

import com.chunkslab.gestures.playeranimator.api.animation.pack.Bone;
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

}
