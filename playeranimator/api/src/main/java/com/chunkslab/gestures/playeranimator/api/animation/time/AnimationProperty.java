package com.chunkslab.gestures.playeranimator.api.animation.time;

import com.chunkslab.gestures.playeranimator.api.animation.animation.Animation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class AnimationProperty {

	@Getter private final Animation animation;
	@Getter private double time = 0;
	@Getter @Setter private double speed;

	public AnimationProperty(Animation animation) {
		this(animation, 1);
	}

	public AnimationProperty(Animation animation, double speed) {
		this.animation = animation;
		this.speed = speed;
	}

	public boolean updateTime() {
		switch (animation.getLoopMode()) {
			case ONCE -> {
				if(time < animation.getLength()) {
					time = Math.min(time + (speed / 20), animation.getLength());
					return true;
				}
			}
			case HOLD -> {
				time = Math.min(time + (speed / 20), animation.getLength());
				return true;
			}
			case LOOP -> {
				time = (time + (speed / 20)) % animation.getLength();
				return true;
			}
		}
		return false;
	}

	public Vector getPositionFrame(String bone) {
		return animation.getPosition(bone, time);
	}

	public EulerAngle getRotationFrame(String bone) {
		return animation.getRotation(bone, time);
	}

}
