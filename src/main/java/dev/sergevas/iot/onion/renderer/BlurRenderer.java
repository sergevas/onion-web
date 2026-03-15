package dev.sergevas.iot.onion.renderer;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class BlurRenderer {

    public static BufferedImage blur(BufferedImage src) {
        float[] matrix = {
                1f / 16, 2f / 16, 1f / 16,
                2f / 16, 4f / 16, 2f / 16,
                1f / 16, 2f / 16, 1f / 16
        };
        Kernel kernel = new Kernel(3, 3, matrix);
        ConvolveOp op = new ConvolveOp(kernel);
        return op.filter(src, null);
    }
}
