package dev.vatuu.test.bossbar;

import dev.vatuu.test.TestStuff;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

public abstract class BossbarDisplay {

    @Getter
    private final NamespacedKey key;
    @Getter @Setter
    private String displayText;

    private String text;
    private BarColor colour = BarColor.PINK;
    private BarStyle style = BarStyle.SOLID;
    private double progress = 1.0D;
    private boolean visible = true;

    private boolean dirty = false;

    public BossbarDisplay(String id, String text) {
        this.displayText = text;
        this.key = new NamespacedKey(TestStuff.INSTANCE, id);
    }

    protected abstract void onTick();

    public BossBar create() {
        BossBar bar = Bukkit.createBossBar(key, displayText, colour, style);
        bar.setProgress(this.progress);
        bar.setVisible(visible);

        return bar;
    }

    public void update() {
        onTick();
        if(verifyDirty()) {
            BossBar bar = Bukkit.getBossBar(key);
            bar.setVisible(visible);
            bar.setProgress(progress);
            bar.setColor(colour);
            bar.setStyle(style);
            bar.setTitle(text);
        }
    }

    public void removeBukkitBar() {
        if(Bukkit.getBossBar(key) != null)
            Bukkit.removeBossBar(key);
    }

    public void setColour(BarColor colour) {
        this.colour = colour;
        makeDirty();
    }

    public void setStyle(BarStyle style) {
        this.style = style;
        makeDirty();
    }

    public void setText(String text) {
        this.text = text;
        makeDirty();
    }

    public void setProgress(double progress) {
        this.progress = progress;
        makeDirty();
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        makeDirty();
    }

    public void makeDirty() {
        this.dirty = true;
    }

    public boolean verifyDirty() {
        boolean tmp = this.dirty;
        this.dirty = false;
        return tmp;
    }

    public BarStyle getStyleSegmented(long value) {
        if(value % 12 == 0)
            return BarStyle.SEGMENTED_12;
        if(value % 6 == 0)
            return BarStyle.SEGMENTED_6;
        if(value % 20 == 0)
            return BarStyle.SEGMENTED_20;
        if(value % 10 == 0)
            return BarStyle.SEGMENTED_10;

        return BarStyle.SOLID;
    }
}
