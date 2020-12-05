package dev.vatuu.test.menus.base;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface ClickRunnable {
    Sound onClick(Player p, Inventory inv, ItemStack stack, ClickType isRightClick);
}
