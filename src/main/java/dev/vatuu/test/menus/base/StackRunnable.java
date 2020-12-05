package dev.vatuu.test.menus.base;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

@FunctionalInterface
public interface StackRunnable {
    boolean onStack(ItemStack base, ItemStack cursor, Player p, Inventory inv);
}
