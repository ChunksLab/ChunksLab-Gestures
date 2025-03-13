package com.chunkslab.gestures.playeranimator.api.model.player;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RotateOptions {
    private boolean rotate;
    private float finalYaw;
    private float currentYaw;
    private float modelRotation = 1.0f;

    public RotateOptions(boolean rotate) {
        this(rotate, -1.0f, Float.NaN);
    }

    public RotateOptions(boolean rotate, float finalYaw, float currentYaw) {
        this.rotate = rotate;
        this.finalYaw = finalYaw;
        this.currentYaw = currentYaw;
    }

    public void rotateYaw() {
        float difference = this.finalYaw - this.currentYaw;
        if (difference > 180.0f) {
            difference -= 360.0f;
        } else if (difference < -180.0f) {
            difference += 360.0f;
        }
        float interpolationFactor = 0.05f;
        float rotationAmount = difference * interpolationFactor;
        rotationAmount = Math.min(rotationAmount, this.modelRotation);
        rotationAmount = Math.max(rotationAmount, -this.modelRotation);
        this.currentYaw += rotationAmount;
        if ((double)Math.abs(difference) < 1.0) {
            this.currentYaw = this.finalYaw;
        }
    }

}
