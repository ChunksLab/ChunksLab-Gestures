package com.chunkslab.gestures.playeranimator.api.skin.parts;

import lombok.Getter;

@Getter
public enum SlimSkinPartOverlay {
    ARM_RIGHT_TOP(44, 32),
    ARM_RIGHT_BOTTOM(47, 16),
    ARM_RIGHT_FRONT(44, 36),
    ARM_RIGHT_BACK(51, 36),
    ARM_RIGHT_TOP_2(44, 44),
    ARM_RIGHT_BOTTOM_2(47, 32),
    ARM_RIGHT_FRONT_2(44, 36),
    ARM_RIGHT_BACK_2(51, 44),
    ARM_LEFT_TOP(52, 48),
    ARM_LEFT_BOTTOM(55, 48),
    ARM_LEFT_FRONT(52, 52),
    ARM_LEFT_BACK(59, 52),
    ARM_LEFT_TOP_2(52, 48),
    ARM_LEFT_BOTTOM_2(55, 48),
    ARM_LEFT_FRONT_2(52, 60),
    ARM_LEFT_BACK_2(59, 60);

    private final int x;
    private final int y;

    SlimSkinPartOverlay(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
