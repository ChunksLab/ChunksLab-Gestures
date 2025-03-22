package com.chunkslab.gestures.playeranimator.api.skin.parts.positions;

import com.chunkslab.gestures.playeranimator.api.skin.parts.SkinPart;

public class SkinPartPosition extends PartPosition {
    public SkinPartPosition(SkinPart part, int offsetX, int offsetY) {
        super(part, offsetX, offsetY);
    }

    public SkinPartPosition(SkinPart part, int offsetX, int offsetY, int slimOffsetX, int slimOffsetY) {
        super(part, offsetX, offsetY, slimOffsetX, slimOffsetY);
    }
}
