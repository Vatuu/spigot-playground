package dev.vatuu.test.items.base;

import dev.vatuu.test.TestStuff;
import dev.vatuu.test.util.Events;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class AbstractServerItem {

    private final Material material;
    private final String key;

    protected boolean stackable = false;
    protected boolean enchantmentGlow = false;
    protected boolean droppable = false;

    public AbstractServerItem(Material material, String key) {
        this.material = material;
        this.key = key;

        setupListeners();
    }

    public abstract void setupItem(ItemStack inOut);

    public Event.Result onRightClick(Player p, ItemStack item, boolean shiftClick) {
        return Event.Result.DEFAULT;
    }
    public Event.Result onLeftClick(Player p, ItemStack item, boolean shiftClick) { return Event.Result.DEFAULT; }

    public ItemStack createItemstack() {
        ItemStack stack = new ItemStack(material);
        ItemMeta meta = stack.getItemMeta();
        NBTEditor.set(stack, key, "serverItem");

        if(enchantmentGlow) {
            meta.addEnchant(Enchantment.LURE, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }

        setupItem(stack);

        return stack;
    }

    private void setupListeners() {
        //Interacting
        Events.listen(TestStuff.INSTANCE, PlayerInteractEvent.class, EventPriority.HIGH, e -> {
            Action a = e.getAction();
            if(e.getMaterial() != Material.AIR && e.getItem() != null) {
                String tag = NBTEditor.getString(e.getItem(), "serverItem");
                if (tag != null && tag.equals(key)) {
                    if(a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)
                        onRightClick(e.getPlayer(), e.getItem(), e.getPlayer().isSneaking());
                    else if(a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK)
                        onLeftClick(e.getPlayer(), e.getItem(), e.getPlayer().isSneaking());
                }
            }
        });

        Events.listen(TestStuff.INSTANCE, PlayerDropItemEvent.class, EventPriority.HIGH, e -> {
            if(!this.droppable) {
                ItemStack stack = e.getItemDrop().getItemStack();
                String tag = NBTEditor.getString(stack, "serverItem");
                if (tag != null && tag.equals(key))
                    e.setCancelled(true);
            }
        });
    }
}
