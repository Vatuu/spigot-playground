package dev.vatuu.test.minigames;

import dev.vatuu.test.util.Tickable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public abstract class MinigameState extends Tickable {

    private final String stateId;
    private final String readableName;

    @Getter(AccessLevel.NONE)
    protected final Minigame minigame;

    public void beginState() {
        this.onBegin();
        this.startTick();
    }

    public void stopState() {
        this.stopTick();
        this.onFinish();
    }

    public boolean allowJoining() {
        return false;
    }

    protected abstract void onBegin();
    protected abstract void onTick();
    protected abstract void onFinish();
}
