package com.chunkslab.gestures.playeranimator.api.skin.parts.positions;

import com.chunkslab.gestures.playeranimator.api.skin.parts.SkinPart;
import com.chunkslab.gestures.playeranimator.api.skin.parts.SkinPartOverlay;
import lombok.Getter;

@Getter
public class SkinOverlayPartPosition extends PartPosition {
    private final SkinPartOverlay partOverlay;

    public SkinOverlayPartPosition(SkinPart part, SkinPartOverlay partOverlay, int offsetX, int offsetY) {
        super(part, offsetX, offsetY);
        this.partOverlay = partOverlay;
    }

    public SkinOverlayPartPosition(SkinPart part, SkinPartOverlay partOverlay, int offsetX, int offsetY, int slimOffsetX, int slimOffsetY) {
        super(part, offsetX, offsetY, slimOffsetX, slimOffsetY);
        this.partOverlay = partOverlay;
    }

}

