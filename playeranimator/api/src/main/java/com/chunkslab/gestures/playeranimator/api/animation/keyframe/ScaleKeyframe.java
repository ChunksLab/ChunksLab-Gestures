package com.chunkslab.gestures.playeranimator.api.animation.keyframe;

import org.bukkit.util.Vector;

public class ScaleKeyframe extends AbstractKeyframe<Vector> {

    public ScaleKeyframe(Vector value) {
        super(value);
    }

    public ScaleKeyframe(Vector value, KeyframeType type) {
        super(value, type);
    }

}