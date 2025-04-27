/*
 * This file is part of ChunksLab-Gestures, licensed under the Apache License 2.0.
 *
 * Copyright (c) amownyy <amowny08@gmail.com>
 * Copyright (c) contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chunkslab.gestures.playeranimator.api.skin;

import com.chunkslab.gestures.playeranimator.api.PlayerAnimator;
import com.chunkslab.gestures.playeranimator.api.skin.images.ImageArea;
import com.chunkslab.gestures.playeranimator.api.skin.images.SkinImage;
import com.chunkslab.gestures.playeranimator.api.skin.images.SkinTexture;
import com.chunkslab.gestures.playeranimator.api.skin.parts.SkinPart;
import com.chunkslab.gestures.playeranimator.api.skin.parts.DefaultSkinPosition;
import com.chunkslab.gestures.playeranimator.api.skin.parts.positions.PartPosition;
import com.chunkslab.gestures.playeranimator.api.skin.parts.positions.SkinOverlayPartPosition;
import com.chunkslab.gestures.playeranimator.api.skin.parts.positions.SkinPosition;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

@RequiredArgsConstructor
public class SkinManager {

    public SkinImage getPlayerSkinPosition(Player player, SkinPosition skinPosition, int resize, boolean withoutOverlay) {
        validateSize(resize);
        SkinTexture skin = (SkinTexture) PlayerAnimator.api.getSkinManager().getSkinTexture(player);
        if (skin == null) return null;
        if (skinPosition == DefaultSkinPosition.BASE) return new SkinImage(skin.getImage(), skin.isSlim());
        BufferedImage positionImage = new BufferedImage(64, 64, 2);
        Graphics2D g2d = positionImage.createGraphics();
        for (PartPosition partPosition : skinPosition.getPartPositions()) {
            BufferedImage partImage;
            if (partPosition instanceof SkinOverlayPartPosition skinOverlayPartPosition) {
                partImage = getSkinPart(skin, skinOverlayPartPosition, resize);
            } else {
                partImage = withoutOverlay ? getSkinPartWithoutOverlay(skin, partPosition.getPart(), resize) : getSkinPart(skin, partPosition.getPart(), resize);
            }
            if (partImage == null) continue;
            int offsetX = partPosition.getOffsetX(skin.isSlimSkin());
            int offsetY = partPosition.getOffsetY(skin.isSlimSkin());
            g2d.drawImage(partImage, null, offsetX, offsetY);
        }
        return new SkinImage(positionImage, skin.isSlim());
    }

    public BufferedImage getSkinPart(SkinTexture skin, SkinOverlayPartPosition skinOverlayPartPosition, int resize) throws IllegalArgumentException {
        BufferedImage partImage;
        BufferedImage image = skin.getImage();
        SkinPart part = skinOverlayPartPosition.getPart();
        ImageArea partArea = part.getArea();
        ImageArea overlayArea = part.getOverlayArea();
        boolean useSmallSkinPart = !skin.isLargeSkin() && part.getSmallSkinPart() != null;
        if (useSmallSkinPart) {
            partArea = part.getSmallSkinPart().getArea();
            overlayArea = part.getSmallSkinPart().getOverlayArea();
        } else if (skin.isSlimSkin() && part.getSlimSkinPart() != null) {
            partArea = part.getSlimSkinPart().getArea();
            overlayArea = part.getSlimSkinPart().getOverlayArea();
        }
        if (!skin.hasOverlay(overlayArea)) {
            return null;
        }
        try {
            partImage = image.getSubimage(skinOverlayPartPosition.getPartOverlay().getX(), skinOverlayPartPosition.getPartOverlay().getY(), partArea.getW(), partArea.getH());
        } catch (RasterFormatException exception) {
            return null;
        }
        if (resize > 1) {
            BufferedImage scaledImage = new BufferedImage(resize, resize, image.getType());
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale((double)resize / (double)partImage.getWidth(), (double)resize / (double)partImage.getHeight());
            AffineTransformOp scaleOp = new AffineTransformOp(affineTransform, 1);
            scaledImage = scaleOp.filter(partImage, scaledImage);
            return scaledImage;
        }
        return partImage;
    }

    public BufferedImage getSkinPart (SkinTexture skin, SkinPart part, int size) {
        BufferedImage image = skin.getImage();
        ImageArea partArea = part.getArea();
        ImageArea overlayArea = part.getOverlayArea();
        boolean useSmallSkinPart = !skin.isLargeSkin() && part.getSmallSkinPart() != null;
        if (useSmallSkinPart) {
            partArea = part.getSmallSkinPart().getArea();
            overlayArea = part.getSmallSkinPart().getOverlayArea();
        } else if (skin.isSlimSkin() && part.getSlimSkinPart() != null) {
            partArea = part.getSlimSkinPart().getArea();
            overlayArea = part.getSlimSkinPart().getOverlayArea();
        }
        BufferedImage partImage = image.getSubimage(partArea.getX(), partArea.getY(), partArea.getW(), partArea.getH());
        for (int x = 0; x < partImage.getWidth(); ++x) {
            for (int y = 0; y < partImage.getHeight(); ++y) {
                int pixel = partImage.getRGB(x, y);
                if (this.isOpaque(pixel)) continue;
                partImage.setRGB(x, y, -16777216);
            }
        }
        if (skin.hasOverlay(overlayArea)) {
            BufferedImage overlayImage = image.getSubimage(overlayArea.getX(), overlayArea.getY(), overlayArea.getW(), overlayArea.getH());
            BufferedImage combinedImage = new BufferedImage(partArea.getW(), partArea.getH(), 2);
            Graphics2D g2d = combinedImage.createGraphics();
            AlphaComposite composite = AlphaComposite.getInstance(3, 1.0f);
            g2d.setComposite(composite);
            g2d.drawImage(partImage, 0, 0, null);
            composite = AlphaComposite.getInstance(3);
            g2d.setComposite(composite);
            g2d.drawImage(overlayImage, 0, 0, null);
            g2d.dispose();
            partImage = combinedImage;
        }
        if (useSmallSkinPart) {
            partImage = this.flipImage(partImage);
        }
        if (size > 1) {
            BufferedImage enlargedPartImage = new BufferedImage(size * partArea.getW(), size * partArea.getH(), image.getType());
            for (int x = 0; x < partArea.getW(); ++x) {
                for (int y = 0; y < partArea.getH(); ++y) {
                    int pixel = partImage.getRGB(x, y);
                    this.drawSquare(enlargedPartImage, x * size, y * size, size, pixel);
                }
            }
            partImage = enlargedPartImage;
        }
        return partImage;
    }

    public BufferedImage getSkinPartWithoutOverlay(SkinTexture skin, SkinPart part, int resize) throws IllegalArgumentException {
        BufferedImage image = skin.getImage();
        ImageArea partArea = part.getArea();
        boolean useSmallSkinPart = !skin.isLargeSkin() && part.getSmallSkinPart() != null;
        if (useSmallSkinPart) {
            partArea = part.getSmallSkinPart().getArea();
        } else if (skin.isSlimSkin() && part.getSlimSkinPart() != null) {
            partArea = part.getSlimSkinPart().getArea();
        }
        BufferedImage partImage = image.getSubimage(partArea.getX(), partArea.getY(), partArea.getW(), partArea.getH());
        if (resize > 1) {
            BufferedImage scaledImage = new BufferedImage(resize, resize, image.getType());
            AffineTransform affineTransform = new AffineTransform();
            affineTransform.scale((double)resize / (double)partImage.getWidth(), (double)resize / (double)partImage.getHeight());
            AffineTransformOp scaleOp = new AffineTransformOp(affineTransform, 1);
            scaledImage = scaleOp.filter(partImage, scaledImage);
            return scaledImage;
        }
        return partImage;
    }

    private BufferedImage flipImage(BufferedImage image) {
        AffineTransform affineTransform = AffineTransform.getScaleInstance(-1.0, 1.0);
        affineTransform.translate(-image.getWidth(null), 0.0);
        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, 1);
        return affineTransformOp.filter(image, null);
    }

    private void drawSquare(BufferedImage image, int x, int y, int size, int pixel) {
        for (int px = 0; px < size; ++px) {
            for (int py = 0; py < size; ++py) {
                image.setRGB(x + px, y + py, pixel);
            }
        }
    }

    public boolean isOpaque(int pixel) {
        return (pixel >> 24 & 0xFF) == 255;
    }

    private void validateSize(double size) {
        if (size < 1.0 || size > 50.0) {
            throw new IllegalArgumentException("size cannot be less than 1 or greater than 50");
        }
    }
}