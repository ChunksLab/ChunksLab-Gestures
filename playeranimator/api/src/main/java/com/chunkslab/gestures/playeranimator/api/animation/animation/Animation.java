package com.chunkslab.gestures.playeranimator.api.animation.animation;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;

public class Animation {

	private final Map<String, Timeline> timelines = new HashMap<>();
	@Getter private final String name;

	@Getter @Setter private double length;
	@Getter @Setter private LoopMode loopMode;
	@Getter @Setter private boolean override;

	public Animation(String name) {
		this.name = name;
	}

	public Timeline getOrCreateTimeline(String bone) {
		if(timelines.containsKey(bone))
			return timelines.get(bone);
		Timeline timeline = new Timeline();
		timelines.put(bone, timeline);
		return timeline;
	}

	public Vector getPosition(String bone, double time) {
		if(!timelines.containsKey(bone))
			return new Vector();
		return timelines.get(bone).getPositionFrame(time);
	}

	public Vector getScale(String bone, double time) {
		if (!timelines.containsKey(bone)) {
			return null;
		}
		return timelines.get(bone).getScaleFrame(time);
	}

	public EulerAngle getRotation(String bone, double time) {
		if(!timelines.containsKey(bone))
			return EulerAngle.ZERO;
		return timelines.get(bone).getRotationFrame(time);
	}

}
