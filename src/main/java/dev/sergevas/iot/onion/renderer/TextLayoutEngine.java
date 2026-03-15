package dev.sergevas.iot.onion.renderer;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public class TextLayoutEngine {

    public static List<TextLayout> layoutText(
            List<String> lines,
            Font font,
            FontRenderContext frc,
            float maxWidth
    ) {
        List<TextLayout> layouts = new ArrayList<>();
        for (String line : lines) {
            AttributedString attributed;
            if (line.isEmpty()) {
                attributed = new AttributedString("\u200B");
            } else {
                attributed = new AttributedString(line);
            }
            attributed.addAttribute(TextAttribute.FONT, font);
            AttributedCharacterIterator it = attributed.getIterator();
            LineBreakMeasurer measurer = new LineBreakMeasurer(it, frc);
            while (measurer.getPosition() < it.getEndIndex()) {
                layouts.add(measurer.nextLayout(maxWidth));
            }
        }
        return layouts;
    }
}
