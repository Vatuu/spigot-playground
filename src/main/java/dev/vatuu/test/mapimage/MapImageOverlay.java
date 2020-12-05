package dev.vatuu.test.mapimage;

import dev.vatuu.test.events.MapImageRerenderEvent;
import dev.vatuu.test.util.Tickable;
import dev.vatuu.test.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public abstract class MapImageOverlay extends Tickable {

    private final static Map<Tuple<Character, Integer>, BufferedImage> characterCache = new HashMap<>();

    private final NamespacedKey image;

    public MapImageOverlay(NamespacedKey key, boolean shouldTick) {
        this.image = key;
        if(shouldTick)
            this.startTick();
    }

    public void destroy() {
        this.stopTick();
    }

    public abstract void render(BufferedImage base);
    public void onTick() { }

    protected int getStringWidth(String text, int scale) {
        return MinecraftFont.Font.getWidth(text) * scale;
    }

    protected int getStringWidth(String text) {
        return this.getStringWidth(text, 1);
    }

    protected void drawStringCentered(int x, int y, BufferedImage img, String text, int colour) {
        this.drawStringCentered(img, x, y, text, colour, 1);
    }

    protected void drawStringCentered(BufferedImage img, int x, int y, String text, int colour, int scale) {
        int xAdj = x - getStringWidth(text, scale) / 2;
        int yAdj = y - (MinecraftFont.Font.getHeight() * scale) / 2;
        this.drawString(img, xAdj, yAdj, text, colour, scale);
    }

    protected void drawString(BufferedImage img, int x, int y, String text, int colour) {
        this.drawString(img, x, y, text, colour, 1);
    }

    protected void drawString(BufferedImage img, int x, int y, String text, int colour, int scale) {
        char[] chars = text.toCharArray();
        int xStart = x;
        int yStart = y;
        for(char c : chars) {
            if(c == '\n') {
                int height = MinecraftFont.Font.getChar(c).getHeight() * scale;
                yStart += height + (2 * scale);
                xStart = x;
                continue;
            }
            int width = MinecraftFont.Font.getChar(c).getWidth() * scale;
            drawCharacter(img, xStart, yStart, c, scale, colour);
            xStart += width + (2 * scale);
        }
    }

    protected void drawCharacter(BufferedImage img, int x, int y, char c, int scale, int colour) {
        BufferedImage letter = getScaledImage(getImage(new Tuple<>(c, colour)), scale);
        Graphics2D g = img.createGraphics();
        g.drawImage(letter, x, y, letter.getWidth(), letter.getHeight(), null);
        g.dispose();
    }

    protected void demandRerender() {
        MapImageRerenderEvent e = new MapImageRerenderEvent(image);
        Bukkit.getServer().getPluginManager().callEvent(e);
    }

    private static BufferedImage getImage(Tuple<Character, Integer> charDat) {
        if(characterCache.containsKey(charDat))
            return characterCache.get(charDat);

        MapFont.CharacterSprite sprite = MinecraftFont.Font.getChar(charDat.getLeft());
        int width = sprite.getWidth(); int height = sprite.getHeight();
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean data = sprite.get(y, x);
                img.setRGB(x, y, data ? 0xFF000000 | charDat.getRight() : 0);
            }
        }

        characterCache.put(charDat, img);
        return img;
    }

    protected static BufferedImage getScaledImage(BufferedImage img, int scale) {
        if(scale > 1) {
            int width = img.getWidth(); int height = img.getHeight();
            BufferedImage scaled = new BufferedImage(width * scale, height * scale, img.getType());
            Graphics2D dim = scaled.createGraphics();
            dim.drawImage(img, 0, 0, width * scale, height * scale, null);
            dim.dispose();
            return scaled;
        }
        return img;
    }
}
