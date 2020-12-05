package dev.vatuu.test.skyraid;

import dev.vatuu.test.minigames.Minigame;
import dev.vatuu.test.minigames.MinigameState;
import dev.vatuu.test.util.TrackableQueue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class SkyraidMinigame extends Minigame {

    private List<Player> teamBlue;
    private List<Player> teamGreen;

    public TrackableQueue<MinigameState> getStateQueue() {
        TrackableQueue<MinigameState> states = new TrackableQueue<>();
        states.add(new LobbyState(this));
        return states;
    }

    @Override
    protected void onMinigameStart() {
        //Load Relevant Data
    }

    @Override
    protected void onMinigameFinish() {
        //Update Player Data
    }

    @Override
    protected void onMinigameRestart() {
        Bukkit.broadcastMessage("Restarting minigame.");
    }

    public void setTeams(List<Player> blue, List<Player> green) {
        this.teamBlue = blue;
        this.teamGreen = green;
    }
}
