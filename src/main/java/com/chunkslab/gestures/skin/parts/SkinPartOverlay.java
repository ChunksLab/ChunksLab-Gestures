package com.chunkslab.gestures.skin.parts;

import lombok.Getter;

@Getter
public enum SkinPartOverlay {
    LEG_RIGHT_TOP(4, 32),
    LEG_RIGHT_BOTTOM(8, 32),
    LEG_RIGHT_OUTSIDE(0, 36),
    LEG_RIGHT_FRONT(4, 36),
    LEG_RIGHT_INSIDE(8, 36),
    LEG_RIGHT_BACK(12, 36),
    LEG_RIGHT_TOP_2(4, 32),
    LEG_RIGHT_BOTTOM_2(8, 32),
    LEG_RIGHT_OUTSIDE_2(0, 44),
    LEG_RIGHT_FRONT_2(4, 44),
    LEG_RIGHT_INSIDE_2(8, 44),
    LEG_RIGHT_BACK_2(12, 44),
    BODY_TOP(20, 32),
    BODY_BOTTOM(28, 32),
    BODY_RIGHT(16, 36),
    BODY_FRONT(20, 36),
    BODY_LEFT(28, 36),
    BODY_BACK(32, 36),
    BODY_TOP_2(20, 44),
    BODY_BOTTOM_2(28, 32),
    BODY_RIGHT_2(16, 44),
    BODY_FRONT_2(20, 44),
    BODY_LEFT_2(28, 44),
    BODY_BACK_2(32, 44),
    ARM_RIGHT_TOP(44, 32),
    ARM_RIGHT_BOTTOM(48, 16),
    ARM_RIGHT_OUTSIDE(40, 36),
    ARM_RIGHT_FRONT(44, 36),
    ARM_RIGHT_INSIDE(48, 36),
    ARM_RIGHT_BACK(52, 36),
    ARM_RIGHT_TOP_2(44, 44),
    ARM_RIGHT_BOTTOM_2(48, 32),
    ARM_RIGHT_OUTSIDE_2(40, 44),
    ARM_RIGHT_FRONT_2(44, 36),
    ARM_RIGHT_INSIDE_2(48, 44),
    ARM_RIGHT_BACK_2(52, 44),
    LEG_LEFT_TOP(4, 48),
    LEG_LEFT_BOTTOM(8, 48),
    LEG_LEFT_OUTSIDE(0, 52),
    LEG_LEFT_FRONT(4, 52),
    LEG_LEFT_INSIDE(8, 52),
    LEG_LEFT_BACK(12, 52),
    LEG_LEFT_TOP_2(4, 48),
    LEG_LEFT_BOTTOM_2(8, 48),
    LEG_LEFT_OUTSIDE_2(0, 60),
    LEG_LEFT_FRONT_2(4, 60),
    LEG_LEFT_INSIDE_2(8, 60),
    LEG_LEFT_BACK_2(12, 60),
    ARM_LEFT_TOP(52, 48),
    ARM_LEFT_BOTTOM(56, 48),
    ARM_LEFT_OUTSIDE(48, 52),
    ARM_LEFT_FRONT(52, 52),
    ARM_LEFT_INSIDE(56, 52),
    ARM_LEFT_BACK(60, 52),
    ARM_LEFT_TOP_2(52, 48),
    ARM_LEFT_BOTTOM_2(56, 48),
    ARM_LEFT_OUTSIDE_2(48, 60),
    ARM_LEFT_FRONT_2(52, 60),
    ARM_LEFT_INSIDE_2(56, 60),
    ARM_LEFT_BACK_2(60, 60);

    private final int x;
    private final int y;

    SkinPartOverlay(int x, int y) {
        this.x = x;
        this.y = y;
    }

}