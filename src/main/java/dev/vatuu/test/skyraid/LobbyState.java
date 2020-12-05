package dev.vatuu.test.skyraid;

import com.google.common.collect.Lists;
import dev.vatuu.test.TestStuff;
import dev.vatuu.test.minigames.Minigame;
import dev.vatuu.test.minigames.TimedMinigameState;
import dev.vatuu.test.util.Events;
import dev.vatuu.test.util.TimeFormat;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LobbyState extends TimedMinigameState {

    private Events joinEvent, leaveEvent;
    private final int minPlayer, maxPlayer;

    public LobbyState(Minigame m) {
        super(m, "test:skyraid:lobby", "Skyraid Lobby",  TimeFormat.MINUTES, 5);
        this.minPlayer = 4;
        this.maxPlayer = 8;
    }

    public void onBegin() {
        this.joinEvent = Events.listen(TestStuff.INSTANCE, PlayerJoinEvent.class, EventPriority.HIGHEST, e -> {
            e.setJoinMessage(String.format("%s joined the Skyraid! [%d/%d]", e.getPlayer().getDisplayName(), Bukkit.getOnlinePlayers().size(), maxPlayer));

        });
        this.leaveEvent = Events.listen(TestStuff.INSTANCE, PlayerQuitEvent.class, EventPriority.HIGHEST, e -> {
            e.setQuitMessage(String.format("%s has abandoned the Skyraid! [%d/%d]", e.getPlayer().getDisplayName(), Bukkit.getOnlinePlayers().size(), maxPlayer));
        });

        this.createGlobalTimerBar("Starting in %remaining%");
    }

    public void tick() {
        if(Bukkit.getOnlinePlayers().size() >= 8) {
            Bukkit.broadcastMessage("8 Players online, starting in 30s");
            updateFinishTime(TimeFormat.SECONDS, 30, false);
            resetTimeQuiet();
        }
        //Scoreboard
    }

    public void onFinish() {
        Random r = new Random();
        List<Player> blue = new ArrayList<>();
        List<Player> green = new ArrayList<>();
        List<Player> all = Lists.newArrayList(Bukkit.getOnlinePlayers());

        List<Integer> indices = IntStream.of(all.size() - 1).boxed().collect(Collectors.toList());
        Collections.shuffle(indices, r);
        boolean startBlue = r.nextBoolean();
        for (int i = 0; i < indices.size(); i++) {
            if(startBlue) {
                blue.add(all.get(i++));
                if(i < indices.size())
                    green.add(all.get(i));
            } else {
                green.add(all.get(i++));
                if(i < indices.size())
                    blue.add(all.get(i));
            }
        }

        ((SkyraidMinigame)minigame).setTeams(blue, green);

        //Change World
    }

    protected void onRestart() {
        Bukkit.broadcastMessage("Not enough players to start the game. (Minimum 4)");
    }

    protected boolean verifyFinishCriteria() {
        return Bukkit.getOnlinePlayers().size() >= minPlayer;
    }

    @Override
    public boolean allowJoining() {
        return true;
    }
}
