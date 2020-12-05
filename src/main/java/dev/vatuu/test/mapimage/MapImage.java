package dev.vatuu.test.mapimage;

import dev.vatuu.test.TestStuff;
import dev.vatuu.test.events.MapImageRerenderEvent;
import dev.vatuu.test.util.Events;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MapImage {

    private final NamespacedKey key;

    private BufferedImage imageBase;
    private BufferedImage imageRendered;

    private final int mapX, mapY;
    private final MapView[][] mapViews;

    private final List<MapImageOverlay> textEntries = new ArrayList<>();
    private Events rerenderEvent;

    public MapImage(String path, NamespacedKey key) {
        try (InputStream s = this.getClass().getClassLoader().getResourceAsStream(path)){
            imageBase = ImageIO.read(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.key = key;
        this.mapX = imageBase.getWidth() / 128;
        this.mapY = imageBase.getHeight() / 128;
        this.mapViews = new MapView[mapX][mapY];
    }

    public void addOverlay(MapImageOverlay entry) {
        this.textEntries.add(entry);
    }

    public void create(World w) {
        generateMapViews(w);
        this.rerenderEvent = Events.listen(TestStuff.INSTANCE, MapImageRerenderEvent.class, l -> {
            if(l.getMapImageId().equals(this.key))
                this.renderImage();
        });
    }

    public void destroy() {
        rerenderEvent.unregister();
        this.imageRendered = null; this.imageBase = null;
    }

    public BufferedImage getImagePart(int x, int y) {
        return imageRendered.getSubimage(x * 128, y * 128, 128, 128);
    }

    public void createDisplay(Location corner, BlockFace facing) {
        for (int i = 0; i < mapY; i++) {
            for (int j = 0; j < mapX; j++) {
                Location loc = corner.clone();
                loc.add(-j, -i, 0);
                ItemFrame frame = (ItemFrame) corner.getWorld().spawnEntity(loc, EntityType.ITEM_FRAME);
                frame.setItem(getMap(mapViews[j][i]));
                frame.setFixed(true);
                frame.setFacingDirection(BlockFace.NORTH, true);
            }
        }
    }

    private ItemStack getMap(MapView v) {
        ItemStack i = new ItemStack(Material.FILLED_MAP);
        MapMeta meta = (MapMeta)i.getItemMeta();
        meta.setMapView(v);
        i.setItemMeta(meta);
        return i;
    }

    private void renderImage() {
        imageRendered = new BufferedImage(imageBase.getWidth(), imageBase.getHeight(), imageBase.getType());
        Graphics2D data = imageRendered.createGraphics();
        data.drawImage(imageBase, 0, 0, imageBase.getWidth(), imageBase.getHeight(), null);
        data.dispose();
        textEntries.forEach(t -> t.render(imageRendered));
    }

    private void generateMapViews(World w) {
        renderImage();
        for (int i = 0; i < mapX; i++) {
            for (int j = 0; j < mapY; j++) {
                MapView view = Bukkit.createMap(w);
                view.getRenderers().clear();
                view.addRenderer(new MapImageRenderer(i, j, this));
                mapViews[i][j] = view;
            }
        }
    }
}
