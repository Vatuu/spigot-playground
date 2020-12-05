package dev.vatuu.test.items;

import dev.vatuu.test.TestStuff;
import dev.vatuu.test.items.base.AbstractServerItem;
import dev.vatuu.test.util.Events;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MinersBackpack extends AbstractServerItem {

    public static final String KEY = "miners_backpack";

    public static final int COUNT_MAX_1 = 27 * 64;
    public static final int COUNT_MAX_2 = 54 * 64;
    public static final int COUNT_MAX_3 = 108 * 64;

    public MinersBackpack() {
        super(Material.BARREL, "miners_backpack");
        this.enchantmentGlow = true;
        this.droppable = true;

        Events.listen(TestStuff.INSTANCE, EntityPickupItemEvent.class, e -> {
            if(e.getEntityType() == EntityType.PLAYER) {
                PlayerInventory p = ((Player)e.getEntity()).getInventory();
            }
        });
    }

    @Override
    public void setupItem(ItemStack inOut) {
        NBTEditor.set(inOut, new int[] { 0, 0, 0 }, "slotLevel");
        NBTEditor.set(inOut, Material.AIR.getKey().getKey(), "slotType0", "slotType1", "slotType2");
        NBTEditor.set(inOut, new int[] { 0, 0, 0 }, "slotContent");
    }

    @Override
    public Event.Result onLeftClick(Player p, ItemStack item, boolean shiftClick) {
        System.out.println("Leftclick");
        if(shiftClick)
            System.out.println("Shift Leftclick");
        return Event.Result.DENY;
    }

    @Override
    public Event.Result onRightClick(Player p, ItemStack item, boolean shiftClick) {
        System.out.println("Rightclick");
        if(shiftClick)
            System.out.println("Shift Rightclick");
        return Event.Result.DENY;
    }

    public static BagContent parse(ItemStack stack) {
        BagContent content = new BagContent();
        int[] level = NBTEditor.getIntArray(stack, "slotLevel");
        content.setLevel(level[0], level[1], level[2]);
        content.setMaterial(
                Material.getMaterial(NBTEditor.getString(stack, "slotType0")),
                Material.getMaterial(NBTEditor.getString(stack, "slotType1")),
                Material.getMaterial(NBTEditor.getString(stack, "slotType2"))
        );
        int[] counts = NBTEditor.getIntArray(stack, "slotContent");
        content.setCount(counts[0], counts[1], counts[2]);
        return content;
    }

    @Getter
    public static class BagContent {
        private int lvl0, lvl1, lvl2;
        private Material mat0, mat1, mat2;
        private int count0, count1, count2;

        public void setLevel(int lvl0, int lvl1, int lvl2) {
            this.lvl0 = lvl0; this.lvl1 = lvl1; this.lvl2 = lvl2;
        }

        public void setMaterial(Material mat0, Material mat1, Material mat2) {
            this.mat0 = mat0; this.mat1 = mat1; this.mat2 = mat2;
        }

        public void setCount(int c0, int c1, int c2) {
            this.count0 = c0; this.count1 = c1; this.count2 = c2;
        }
    }
}
