package dev.vatuu.test.minigames;

import dev.vatuu.test.TestStuff;
import dev.vatuu.test.util.Events;
import dev.vatuu.test.util.TrackableQueue;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public abstract class Minigame {

    private final TrackableQueue<MinigameState> queue;
    private MinigameState currentState;

    private Events joinEvent;

    public Minigame() {
        this.queue = getStateQueue();
        this.currentState = queue.getCurrent();
        joinEvent = Events.listen(TestStuff.INSTANCE, AsyncPlayerPreLoginEvent.class, (e) -> {
            if(!currentState.allowJoining()) {
                e.setKickMessage("You cannot join this minigame in this state.");
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            }
        });
        this.onMinigameStart();
        this.currentState.beginState();
    }


    public void progressStateQueue() {
        currentState.stopState();
        currentState = queue.getNext();
        if(currentState == null) {
            this.onMinigameFinish();
            this.joinEvent.unregister();
        }

    }

    public void revertStateQueue() {
        currentState.stopState();
        currentState = queue.getPrevious();
        if(currentState == null) {
            restart();
        }
        currentState.beginState();
    }

    public void restart() {
        if(currentState != null)
            currentState.stopState();
        this.onMinigameRestart();
        queue.setIndex(0);
        this.currentState = queue.getCurrent();
        currentState.beginState();
    }

    protected abstract void onMinigameStart();
    protected abstract void onMinigameFinish();
    protected abstract void onMinigameRestart();

    public abstract TrackableQueue<MinigameState> getStateQueue();
}
