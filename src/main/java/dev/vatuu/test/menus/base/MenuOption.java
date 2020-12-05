package dev.vatuu.test.menus.base;

import com.google.common.collect.Lists;
import dev.vatuu.test.TestStuff;
import dev.vatuu.test.util.Events;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MenuOption {

    protected ItemStack visual;
    private int slot;

    private boolean takeable = false;
    private boolean stackable = false;

    private ClickRunnable runnable = (p, inv, stack, right) -> null;
    private StackRunnable stackRunnable = (base, cursor, player, inv) -> true;

    private Events clickListener, moveListener;

    public MenuOption(Material m,  int size, boolean glow, int customModel, String title, String[] lore, int slot) {
        this.visual = new ItemStack(m, size);
        ItemMeta meta = visual.getItemMeta();
        if(glow) {
            meta.addEnchant(Enchantment.LURE, 1, false);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        if(customModel != 0)
            meta.setCustomModelData(customModel);

        meta.setDisplayName(title);
        meta.setLore(Lists.newArrayList(lore));
        visual.setItemMeta(meta);
        this.slot = slot;
    }

    public MenuOption(Material m, String title, String[] lore, int slot) {
        this(m, 1, false, 0, title, lore, slot);
    }

    public MenuOption(ItemStack stack, String title, String[] lore, int slot) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(title);
        if(lore != null)
            meta.setLore(Lists.newArrayList(lore));
        stack.setItemMeta(meta);
        this.visual = stack;
        this.slot = slot;
    }

    public MenuOption moveOptions(boolean takeable, boolean stackable) {
        this.takeable = takeable;
        this.stackable = stackable;
        return this;
    }

    public MenuOption clickRunnable(ClickRunnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public MenuOption stackRunnable(StackRunnable runnable) {
        this.stackRunnable = runnable;
        return this;
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack get(Player p, Inventory inv) {
        registerListeners(p, inv);
        return visual;
    }

    private void registerListeners(Player p, Inventory inv) {
        this.clickListener = Events.listen(TestStuff.INSTANCE, InventoryClickEvent.class, e -> {
            if(e.getWhoClicked().equals(p))
                if (e.getClickedInventory() == inv && e.getSlot() == this.slot)
                    if (e.getCursor() != null && stackable) {
                        e.setCancelled(stackRunnable.onStack(e.getCurrentItem(), e.getCursor(), p, inv));
                    } else if (!this.takeable) {
                        e.setCancelled(true);
                        Sound s = runnable.onClick(p, e.getClickedInventory(), e.getCurrentItem(), e.getClick());
                        if (s != null)
                            p.playSound(p.getLocation(), s, SoundCategory.VOICE, 1, 1);
                    }
        });
    }

    public void unregisterListeners() {
        this.clickListener.unregister();
        this.moveListener.unregister();
    }
}
