package com.chunkslab.gestures.playeranimator.api.skin.parts.positions;

import java.util.List;

public interface SkinPosition {
    default int getImageWidth(boolean slim) {
        return this.getPartPositions().stream().mapToInt(pos -> pos.getMaxX(slim)).max().getAsInt();
    }

    default int getImageHeight(boolean slim) {
        return this.getPartPositions().stream().mapToInt(pos -> pos.getMaxY(slim)).max().getAsInt();
    }

    List<PartPosition> getPartPositions();
}