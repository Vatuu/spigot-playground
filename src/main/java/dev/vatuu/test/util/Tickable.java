package dev.vatuu.test.util;

import dev.vatuu.test.TestStuff;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public abstract class Tickable {

    private BukkitTask tickCallback;

    protected abstract void onTick();

    public void startTick() {
        if(tickCallback == null || tickCallback.isCancelled())
            tickCallback = Bukkit.getScheduler().runTaskTimer(TestStuff.INSTANCE, this::onTick, 0, 0);
    }

    public void stopTick() {
        if(tickCallback != null && !tickCallback.isCancelled())
            tickCallback.cancel();
    }
}
