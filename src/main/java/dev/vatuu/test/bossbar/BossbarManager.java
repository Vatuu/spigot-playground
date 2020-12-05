package dev.vatuu.test.bossbar;

import dev.vatuu.test.TestStuff;
import dev.vatuu.test.util.Events;
import dev.vatuu.test.util.Tickable;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class BossbarManager extends Tickable {

    private final Map<BossbarDisplay, Player> playerBars = new HashMap<>();
    private final Map<BossbarDisplay, Boolean> globalBars = new HashMap<>();

    public BossbarManager() {
        Events.listen(TestStuff.INSTANCE, PlayerJoinEvent.class, (p) -> addPlayerToGlobal(p.getPlayer()));
        Events.listen(TestStuff.INSTANCE, PlayerQuitEvent.class, (p) -> removePlayerFromGlobal(p.getPlayer()));
        this.startTick();
    }

    public void registerPlayerBar(BossbarDisplay bar, Player p) {
        if(playerBars.containsKey(bar) && playerBars.get(bar) == p)
            bar.removeBukkitBar();
        BossBar raw = bar.create();
        raw.addPlayer(p);
        if(!playerBars.containsValue(p))
            globalBars.forEach((b, l) -> {
                if(l) Bukkit.getBossBar(b.getKey()).removePlayer(p);
            });
        playerBars.put(bar, p);
    }

    public void unregisterPlayerBar(BossbarDisplay bar) {
        bar.removeBukkitBar();
        Player p = playerBars.remove(bar);
        if(!playerBars.containsValue(p))
            globalBars.forEach((b, l) -> {
                if(l) Bukkit.getBossBar(b.getKey()).addPlayer(p);
            });
    }

    public void registerGlobalBar(BossbarDisplay bar, boolean loner) {
        if(playerBars.containsKey(bar))
            bar.removeBukkitBar();
        BossBar raw = bar.create();
        Bukkit.getOnlinePlayers().forEach(raw::addPlayer);
        globalBars.put(bar, loner);
    }

    public void unregisterGlobalBar(BossbarDisplay bar) {
        bar.removeBukkitBar();
        globalBars.remove(bar);
    }

    public void addPlayerToGlobal(Player p) {
        globalBars.forEach((d, l) -> Bukkit.getBossBar(d.getKey()).addPlayer(p));
    }

    public void removePlayerFromGlobal(Player p) {
        globalBars.forEach((d, l) -> Bukkit.getBossBar(d.getKey()).removePlayer(p));
    }

    @Override
    protected void onTick() {
        playerBars.forEach((b, p) -> b.update());
        globalBars.forEach((b, p) -> b.update());
    }
}
