package dev.vatuu.test.bossbar;

import dev.vatuu.test.util.TimeFormat;
import org.bukkit.boss.BarStyle;

import java.util.function.Consumer;

public class BossbarTimer extends BossbarDisplay {

    private String displayText;
    private TimeFormat format;
    private long length;
    private long tracked = 0;

    private Consumer<BossbarTimer> tick, finish;

    private boolean running = true;

    private BossbarTimer(String id, String displayText, TimeFormat format, long length, Consumer<BossbarTimer> tick, Consumer<BossbarTimer> finish) {
        super(id, "");
        this.displayText = displayText;
        this.format = format;
        this.length = format.getTicks(length);
        this.tick = tick;
        this.finish = finish;
        this.setText(formatText());
        this.setStyle(this.getStyleSegmented(length));
    }

    public void setTracked(long tracked) {
        this.tracked = tracked;
    }

    public void setRunning(boolean r) {
        this.running = r;
    }

    public void updateTime(TimeFormat format, long amount, boolean reset) {
        this.format = format;
        this.length = format.getTicks(amount);
        if(reset)
            restart();
    }

    public void restart() {
        this.tracked = 0;
        this.running = true;
    }

    public long getTicks() {
        return tracked;
    }

    @Override
    protected void onTick() {
        if(!running)
            return;

        this.setText(formatText());
        this.setProgress(1.0D - ((double)tracked / length));
        if(tracked >= length) {
            this.running = false;
            if(this.finish != null)
                this.finish.accept(this);
        }
        if(this.tick != null)
            this.tick.accept(this);
        tracked++;
    }

    private String formatText() {
        return this.displayText.replace("%time%", format.getFormattedString(length - tracked));
    }

    public static class Builder {

        private final String id, text;
        private Consumer<BossbarTimer> onTick, onFinish;

        public Builder(String id, String text) {
            this.id = id;
            this.text = text;
        }

        public Builder onTick(Consumer<BossbarTimer> runnable) {
            this.onTick = runnable;
            return this;
        }

        public Builder onFinish(Consumer<BossbarTimer> runnable) {
            this.onFinish = runnable;
            return this;
        }

        public BossbarTimer build(TimeFormat format, long amount) {
            return new BossbarTimer(id, text, format, amount, onTick, onFinish);
        }
    }
}
