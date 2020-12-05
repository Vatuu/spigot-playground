package dev.vatuu.test.bossbar;

import dev.vatuu.test.minigames.TimedMinigameState;
import dev.vatuu.test.util.StringUtils;
import dev.vatuu.test.util.Tuple;
import lombok.Setter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

public class BossbarTimedState extends BossbarDisplay {

    private TimedMinigameState state;

    @Setter
    private boolean filling;

    public BossbarTimedState(String id, String text, BarColor colour, boolean filling, TimedMinigameState state) {
        this(id, text, colour, null, filling, state);
    }

    public BossbarTimedState(String id, String text, BarColor colour, BarStyle style, boolean filling, TimedMinigameState state) {
        super(id, text);
        this.state = state;
        this.filling = filling;
        this.setText(formatString());
        this.setColour(colour);
        if(style == null)
            this.getStyleSegmented(state.getTicks());
        else
            this.setStyle(style);
    }

    @Override
    protected void onTick() {
        this.setText(formatString());
        double prog = (double)state.getTicksPassed() / state.getTicks();
        this.setProgress(filling ? prog : 1.0 - prog);
    }

    private String formatString() {
        Tuple<String, String> timeRemaining = new Tuple<>("%remaining%", state.getFormat().getFormattedString(state.getRemainingTime()));
        Tuple<String, String> timePassed = new Tuple<>("%passed%", state.getFormat().getFormattedString(state.getTicksPassed()));
        return StringUtils.formatString(getDisplayText(), timeRemaining, timePassed);
    }
}
