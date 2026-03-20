package dev.sergevas.iot.onion.renderer;

import dev.sergevas.iot.onion.OnionWebException;
import jakarta.enterprise.context.ApplicationScoped;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ImageOverlayService {

    private final Font font;
    private final Color textColor;

    public ImageOverlayService() {
        try {
            this.font = Font.createFont(Font.TRUETYPE_FONT,
                    Objects.requireNonNull(getClass().getResourceAsStream("/META-INF/resources/NotoSans-Regular.ttf"))
            ).deriveFont(52f);
        } catch (Exception e) {
            throw new OnionWebException("Unable to create image font", e);
        }
        this.textColor = Color.WHITE;
    }

    public byte[] render(byte[] inputImageBytes, String outImageExtension, List<String> message) {
        byte[] outputImageByte;
        try (var bos = new ByteArrayOutputStream()) {
            var img = ImageIO.read(new ByteArrayInputStream(inputImageBytes));
            Graphics2D g = img.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            FontRenderContext frc = g.getFontRenderContext();
            float maxWidth = img.getWidth() * 0.4f;
            List<TextLayout> lines = TextLayoutEngine.layoutText(message, font, frc, maxWidth);
            float lineHeight = font.getSize2D() * 1.3f;
            int x = 40;
            int y = 60;
            int boxWidth = (int) maxWidth;
            int boxHeight = (int) (lines.size() * lineHeight) + 40;
            BufferedImage region = img.getSubimage(x, y, boxWidth, boxHeight);
            BufferedImage blurred = BlurRenderer.blur(region);
            g.drawImage(blurred, x, y, null);
            g.setColor(new Color(0, 0, 0, 120));
            g.fillRoundRect(x, y, boxWidth, boxHeight, 20, 20);
            g.setColor(Color.WHITE);
            g.drawRoundRect(x, y, boxWidth, boxHeight, 20, 20);
            g.setColor(textColor);
            float textY = y + 30;
            for (TextLayout layout : lines) {
                textY += layout.getAscent();
                layout.draw(g, x + 20, textY);
                textY += layout.getDescent() + layout.getLeading();
            }
            g.dispose();
            ImageIO.write(img, outImageExtension, bos);
            outputImageByte = bos.toByteArray();
        } catch (Exception e) {
            throw new OnionWebException("Unable to render image overlay", e);
        }
        return outputImageByte;
    }
}
