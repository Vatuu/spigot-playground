package dev.vatuu.test.menus.base;

import dev.vatuu.test.TestStuff;
import dev.vatuu.test.util.BannerBuilder;
import dev.vatuu.test.util.Events;
import io.github.bananapuncher714.nbteditor.NBTEditor;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractMenuScreen {

    private final InventoryType type;
    private final int chestSize;
    private List<MenuOption> options;

    private final String title;

    private final Player opening;

    public AbstractMenuScreen(Player opening, InventoryType type, String title) {
        this.type = type;
        this.chestSize = 0;
        this.opening = opening;
        this.title = title;
    }

    public AbstractMenuScreen(Player opening, int rows, String title) {
        this.type = InventoryType.CHEST;
        this.chestSize = rows * 9;
        this.opening = opening;
        this.title = title;
    }

    public abstract Map<Material, Integer> populateDecoration(Player p);
    public abstract List<MenuOption> populateOptions(Player p);
    public abstract void onClose(Player p, Inventory inv);


    public void openInventory() {
        Inventory inv = chestSize == 0
                ? Bukkit.createInventory(null, this.type, title)
                : Bukkit.createInventory(null, chestSize, title);

        populateDecoration(opening).forEach((m, s) -> {
            inv.setItem(s, new ItemStack(m, 1));
        });

        this.options = populateOptions(this.opening);
        this.options.forEach(o -> inv.setItem(o.getSlot(), o.get(this.opening, inv)));

        Events closeListener = Events.listen(TestStuff.INSTANCE, InventoryCloseEvent.class, e -> {
            if(e.getPlayer().equals(opening) && e.getInventory() == inv) {
                this.options.forEach(MenuOption::unregisterListeners);
                this.onClose(opening, inv);
            }
        });

        opening.openInventory(inv);
        closeListener.unregister();
        System.out.println("Closed");
    }

    public List<Integer> findServerItems(String key, Inventory inv) {
        List<Integer> slots = new ArrayList<>();
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack s = inv.getItem(i);
            if(s.getType() != Material.AIR && NBTEditor.contains(s, "serverItem")) {
                String tag = NBTEditor.getString(s, "serverItem");
                if(tag.equals(key))
                    slots.add(i);
            }
        }
        return slots;
    }

    public List<MenuOption> getBannerDisplay(int value, int displayLength, int startSlot) {
        List<MenuOption> options = new ArrayList<>();
        String formatted = formatValue(value, displayLength);
        char[] digits = formatted.toCharArray();
        for (int i = 0; i < displayLength; i++) {
            int digit = digits[digits.length - (i + 1)];
            options.add(new MenuOption(
                    BannerBuilder.Numeric.values()[digit].getBannerItem(DyeColor.WHITE, DyeColor.BLACK),
                    formatted,
                    null,
                    startSlot + i
            ).moveOptions(false, false).clickRunnable((p, inv, stack, clickType) -> Sound.BLOCK_STONE_BUTTON_CLICK_OFF));
        }
        return options;
    }

    private String formatValue(int value, int length) {
        StringBuilder number = new StringBuilder(String.valueOf(value));
        if(number.length() < length)
            for(int i = 0; i < (length - number.length()); i++)
                number.insert(0, "0");
        else if(number.length() > length)
            for(int i = 0; i < (number.length() - length); i++)
                number.deleteCharAt(0);
        return number.toString();
    }
}
