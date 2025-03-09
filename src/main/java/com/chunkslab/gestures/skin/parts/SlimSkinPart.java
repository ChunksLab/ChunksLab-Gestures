package com.chunkslab.gestures.skin.parts;

import com.chunkslab.gestures.skin.images.ImageArea;
import lombok.Getter;

@Getter
public enum SlimSkinPart {
    ARM_RIGHT_TOP(44, 16, 3, 4),
    ARM_RIGHT_BOTTOM(47, 16, 3, 4),
    ARM_RIGHT_FRONT(44, 20, 3, 8),
    ARM_RIGHT_BACK(51, 20, 3, 8),
    ARM_RIGHT_TOP_2(44, 16, 3, 4),
    ARM_RIGHT_BOTTOM_2(47, 16, 3, 4),
    ARM_RIGHT_FRONT_2(44, 28, 3, 4),
    ARM_RIGHT_BACK_2(51, 28, 3, 4),
    ARM_LEFT_TOP(36, 48, 3, 4),
    ARM_LEFT_BOTTOM(39, 48, 3, 4),
    ARM_LEFT_FRONT(36, 52, 3, 8),
    ARM_LEFT_BACK(43, 52, 3, 8),
    ARM_LEFT_TOP_2(36, 48, 3, 4),
    ARM_LEFT_BOTTOM_2(39, 48, 3, 4),
    ARM_LEFT_FRONT_2(36, 60, 3, 4),
    ARM_LEFT_BACK_2(43, 60, 3, 4);

    private final ImageArea area;
    private final ImageArea overlayArea;

    SlimSkinPart(int x, int y, int w, int h) {
        this.area = new ImageArea(x, y, w, h);
        SlimSkinPartOverlay overlay = SlimSkinPartOverlay.valueOf(this.name());
        this.overlayArea = new ImageArea(overlay.getX(), overlay.getY(), w, h);
    }

}