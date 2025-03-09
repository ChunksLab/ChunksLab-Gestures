package com.chunkslab.gestures.skin.images;

import lombok.Getter;

import java.awt.image.BufferedImage;

public class SkinTexture extends SkinImage {

    private static final ImageArea[] RIGHT_DEAD_AREAS = {
            new ImageArea(32, 0, 8, 8),
            new ImageArea(56, 0, 8, 8),
            new ImageArea(36, 16, 8, 4),
            new ImageArea(52, 16, 12, 4),
            new ImageArea(56, 20, 8, 12)
    };

    @Getter
    private final boolean slimSkin;
    @Getter
    private final boolean defaultSkin;
    @Getter
    private final boolean largeSkin;
    private Boolean hasOverlay; // Nullable Boolean for lazy evaluation

    public SkinTexture(BufferedImage image, boolean slimSkin, boolean defaultSkin) {
        super(image, slimSkin);
        validateImageDimensions(image);
        this.slimSkin = slimSkin;
        this.defaultSkin = defaultSkin;
        this.largeSkin = image.getHeight() == 64;
    }

    private void validateImageDimensions(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width != 64 || (height != 32 && height != 64)) {
            throw new IllegalArgumentException("Invalid image dimensions: " + width + "x" + height);
        }
    }

    public boolean hasOverlay(ImageArea overlayArea) {
        if (hasOverlay == null) {
            hasOverlay = largeSkin || !isDeadAreaOpaque(getImage());
        }
        return hasOverlay && (overlayArea.getY() < 32 || largeSkin);
    }

    private boolean isDeadAreaOpaque(BufferedImage skinImage) {
        for (ImageArea deadArea : RIGHT_DEAD_AREAS) {
            if (!isAreaOpaque(skinImage, deadArea)) {
                return false;
            }
        }
        return true;
    }

    private boolean isAreaOpaque(BufferedImage image, ImageArea area) {
        for (int x = area.getX(); x < area.getX() + area.getW(); x++) {
            for (int y = area.getY(); y < area.getY() + area.getH(); y++) {
                if (!isOpaque(image.getRGB(x, y))) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isOpaque(int pixel) {
        return (pixel >>> 24) == 0xFF;
    }

}