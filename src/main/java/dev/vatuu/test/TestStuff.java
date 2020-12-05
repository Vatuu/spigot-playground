package dev.vatuu.test;

import dev.vatuu.test.bossbar.BossbarManager;
import dev.vatuu.test.items.MinersBackpack;
import dev.vatuu.test.mapimage.MapImage;
import dev.vatuu.test.mapimage.overlay.MapOverlayText;
import dev.vatuu.test.mapimage.overlay.MapOverlayTimer;
import dev.vatuu.test.mapimage.overlay.MapOverlayIcon;
import dev.vatuu.test.sam.impl.Player1pxHeadModel;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.io.File;

public final class TestStuff extends JavaPlugin implements CommandExecutor {

    public static TestStuff INSTANCE;

    public MinersBackpack itemMinersBackpack;
    private Player1pxHeadModel samTestHead;
    private MapImage image;

    public static BossbarManager bossbarManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.itemMinersBackpack = new MinersBackpack();
        bossbarManager = new BossbarManager();
    }

    @Override
    public void onDisable() {
        INSTANCE = null;
        File worldData = new File(Bukkit.getServer().getWorld("world").getWorldFolder(), "data");
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (label.equalsIgnoreCase("giveBackpack")) {
                p.getInventory().addItem(itemMinersBackpack.createItemstack());
                return true;
            }

            if(label.equalsIgnoreCase("summonSam")) {
                if(samTestHead != null)
                    samTestHead.hide();

                this.samTestHead = new Player1pxHeadModel(p.getLocation(), 9, 0, 0);
                this.samTestHead.spawn();

                return true;
            }

            if(label.equalsIgnoreCase("moveSam")) {
                if(samTestHead == null) {
                    sender.sendMessage("No SAM Model exists currently.");
                    return true;
                }

                if(args.length == 0) {
                    sender.sendMessage("Need argument.");
                    return true;
                }

                int part = Integer.parseInt(args[0]);

                switch(part) {
                    case 0:
                        this.samTestHead.getEyelidRightUpper().translate(new Vector(0, 0.5 / 16, 0), false);
                        break;
                    case 1:
                        this.samTestHead.getEyelidRightLower().translate(new Vector(0, -(0.5 / 16), 0), false);
                        break;
                    case 2:
                        this.samTestHead.getEyelidLeftUpper().translate(new Vector(0, (0.5 / 16), 0), false);
                        break;
                    case 3:
                        this.samTestHead.getEyelidLeftLower().translate(new Vector(0, -(0.5 / 16), 0), false);
                        break;
                }

                return true;
            }

            if(label.equalsIgnoreCase("createTestMap")) {
                MapImage image = new MapImage("mapimages/background4x3.png", new NamespacedKey(this, "testimage"));
                image.addOverlay(new MapOverlayText(15, 15, "Test String with\nlinebreak.", 5, new NamespacedKey(this, "testimage")));
                image.addOverlay(new MapOverlayIcon(MapOverlayIcon.Icons.REDSTONE_48, 15, 100, 5, new NamespacedKey(this, "testimage")));
                image.create(p.getWorld());
                image.createDisplay(p.getLocation(), BlockFace.NORTH);
                return true;
            }

            if(label.equalsIgnoreCase("createTimerMap")) {
                MapImage image = new MapImage("mapimages/background4x3.png", new NamespacedKey(this, "testTimer"));
                image.addOverlay(new MapOverlayTimer(new NamespacedKey(this, "testTimer"), 5));
                image.create(p.getWorld());
                image.createDisplay(p.getLocation(), BlockFace.NORTH);
                return true;
            }
        }
        return false;
    }
}
