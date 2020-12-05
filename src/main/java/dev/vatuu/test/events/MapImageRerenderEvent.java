package dev.vatuu.test.events;

import dev.vatuu.test.mapimage.MapImage;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class MapImageRerenderEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private NamespacedKey mapImageId;

    public MapImageRerenderEvent(NamespacedKey key) {
        this.mapImageId = key;
    }

    public NamespacedKey getMapImageId() {
        return mapImageId;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
