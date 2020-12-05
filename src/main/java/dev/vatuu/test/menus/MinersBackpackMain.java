package dev.vatuu.test.menus;

import dev.vatuu.test.items.MinersBackpack;
import dev.vatuu.test.menus.base.AbstractMenuScreen;
import dev.vatuu.test.menus.base.MenuOption;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MinersBackpackMain extends AbstractMenuScreen {

    private MinersBackpack.BagContent content;

    public MinersBackpackMain(Player p) {
        super(p,6, "Miners Backpack");
        List<Integer> slots = this.findServerItems(MinersBackpack.KEY, p.getInventory());
        if(slots.isEmpty())
            System.out.println("BAG IS GONE!");
        content = MinersBackpack.parse(p.getInventory().getItem(slots.get(0)));
    }

    @Override
    public Map<Material, Integer> populateDecoration(Player p) {
        return null;
    }

    @Override
    public List<MenuOption> populateOptions(Player p) {
        List<MenuOption> options = new ArrayList<>();
        options.add(
                new MenuOption(
                        content.getMat0(),
                        Math.min(content.getCount0(), 64),
                        false,
                        0,
                        "Material 1",
                        null,
                        20)
                .moveOptions(false, true)
                .clickRunnable((player, inv, stack, clickType) ->  Sound.BLOCK_ANVIL_HIT ));
        options.addAll(getBannerDisplay(content.getCount0(), 4, 22));

        options.add(
                new MenuOption(
                        content.getMat1(),
                        Math.min(content.getCount1(), 64),
                        false,
                        0,
                        "Material 2",
                        null,
                        29)
                        .moveOptions(false, true)
                        .clickRunnable((player, inv, stack, clickType) ->  Sound.BLOCK_ANVIL_HIT ));
        options.addAll(getBannerDisplay(content.getCount1(), 4, 31));

        options.add(
                new MenuOption(
                        content.getMat2(),
                        Math.min(content.getCount2(), 64),
                        false,
                        0,
                        "Material 3",
                        null,
                        38)
                        .moveOptions(false, true)
                        .clickRunnable((player, inv, stack, clickType) ->  Sound.BLOCK_ANVIL_HIT ));
        options.addAll(getBannerDisplay(content.getCount2(), 4, 40));

        return options;
    }

    @Override
    public void onClose(Player p, Inventory inv) {
        
    }
}
