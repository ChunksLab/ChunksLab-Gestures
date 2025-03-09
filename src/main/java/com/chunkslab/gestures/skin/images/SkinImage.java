package com.chunkslab.gestures.skin.images;

import com.chunkslab.gestures.api.util.LogUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Getter
@AllArgsConstructor
public class SkinImage {

    private final BufferedImage image;
    private boolean slim;

    @SneakyThrows
    public byte[] toByteArray() {
        byte[] byArray;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", baos);
            byArray = baos.toByteArray();
        } catch (Throwable throwable) {
            try {
                try {
                    baos.close();
                } catch (Throwable throwable2) {
                    throwable.addSuppressed(throwable2);
                }
                throw throwable;
            } catch (IOException e) {
                LogUtils.warn("An unexpected error occured while writing skin image data to byte array", e);
                return new byte[0];
            }
        }
        baos.close();
        return byArray;
    }

    public String toDataUri() {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(this.toByteArray());
    }
}