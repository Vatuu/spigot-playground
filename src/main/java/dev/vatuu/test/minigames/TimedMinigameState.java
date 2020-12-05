package dev.vatuu.test.minigames;

import dev.vatuu.test.TestStuff;
import dev.vatuu.test.bossbar.BossbarTimedState;
import dev.vatuu.test.util.TimeFormat;
import lombok.Getter;
import org.bukkit.boss.BarColor;

@Getter
public abstract class TimedMinigameState extends MinigameState {

    private long ticks;
    private long ticksPassed = 0;
    private TimeFormat format;

    private BossbarTimedState bar;

    public TimedMinigameState(Minigame game, String id, String name, TimeFormat format, long amount) {
        super(id, name, game);
        updateFinishTime(format, amount, false);
    }

    @Override
    protected void onTick() {
        this.tick();
        if(ticksPassed >= ticks) {
            if (verifyFinishCriteria())
                this.minigame.progressStateQueue();
            else {
                this.onRestart();
                this.ticksPassed = 0;
            }
        }
        this.ticksPassed++;
    }

    public void updateFinishTime(TimeFormat format, long amount, boolean resetProgress) {
        this.ticks = format.getTicks(amount);
        this.format = format;
        if(resetProgress) {
            onRestart();
            this.ticksPassed = 0;
        }
    }

    public void resetTimeQuiet() {
        this.ticksPassed = 0;
    }

    public long getRemainingTime() {
       return this.ticks - this.ticksPassed;
    }

    public void createGlobalTimerBar(String text) {
        if (bar != null)
            return;
        this.bar = new BossbarTimedState(this.getStateId() + "_timer", text, BarColor.YELLOW, false, this);
        TestStuff.bossbarManager.registerGlobalBar(bar, false);
    }

    public void removeGlobalTimerBar() {
        if(bar == null)
            return;
        TestStuff.bossbarManager.unregisterGlobalBar(bar);
        this.bar = null;
    }

    @Override
    protected abstract void onBegin();
    @Override
    protected abstract void onFinish();
    protected abstract void tick();
    protected abstract void onRestart();
    protected abstract boolean verifyFinishCriteria();
}
