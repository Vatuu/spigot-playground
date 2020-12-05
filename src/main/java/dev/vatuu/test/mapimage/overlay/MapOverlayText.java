package dev.vatuu.test.mapimage.overlay;

import dev.vatuu.test.mapimage.MapImageOverlay;
import org.bukkit.NamespacedKey;

import java.awt.image.BufferedImage;

public class MapOverlayText extends MapImageOverlay {

    private final String text;
    private final int scale;
    private final int x, y;

    public MapOverlayText(int x, int y, String text, int scale, NamespacedKey key) {
        super(key, false);
        this.text = text;
        this.scale = scale;
        this.x = x; this.y = y;
    }

    @Override
    public void render(BufferedImage base) {
        this.drawString(base, x, y, text, 0x00000000, scale);
    }
}
