package com.chunkslab.gestures.skin.parts.positions;

import com.chunkslab.gestures.skin.images.ImageArea;
import com.chunkslab.gestures.skin.parts.SkinPart;
import lombok.Getter;

public abstract class PartPosition {
    @Getter
    private final SkinPart part;
    private final int offsetX;
    private final int offsetY;
    private final int slimOffsetX;
    private final int slimOffsetY;

    public PartPosition(SkinPart part, int offsetX, int offsetY) {
        this(part, offsetX, offsetY, offsetX, offsetY);
    }

    public PartPosition(SkinPart part, int offsetX, int offsetY, int slimOffsetX, int slimOffsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.slimOffsetX = slimOffsetX;
        this.slimOffsetY = slimOffsetY;
        this.part = part;
    }

    public int getOffsetX(boolean slim) {
        return slim ? this.slimOffsetX : this.offsetX;
    }

    public int getOffsetY(boolean slim) {
        return slim ? this.slimOffsetY : this.offsetY;
    }

    int getMaxX(boolean slim) {
        return this.getOffsetX(slim) + this.getImageArea(slim).getW();
    }

    int getMaxY(boolean slim) {
        return this.getOffsetY(slim) + this.getImageArea(slim).getH();
    }

    public ImageArea getImageArea(boolean slim) {
        return slim && this.part.getSlimSkinPart() != null ? this.part.getSlimSkinPart().getArea() : this.part.getArea();
    }

}