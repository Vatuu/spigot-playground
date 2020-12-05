package dev.vatuu.test.mapimage;

import org.bukkit.entity.Player;
import org.bukkit.map.*;

import java.awt.image.BufferedImage;

public class MapImageRenderer extends MapRenderer {

    private BufferedImage image;
    private final int x, y;
    private final MapImage map;

    public MapImageRenderer(int x, int y, MapImage map) {
        this.x = x;
        this.y = y;
        this.map = map;
    }

    @Override
    public void render(MapView m, MapCanvas canvas, Player p) {
        BufferedImage fetched = map.getImagePart(this.x, this.y);
        if(image == null || !image.equals(fetched)) {
            this.image = fetched;
            canvas.drawImage(0, 0, fetched);
        }
    }
}
