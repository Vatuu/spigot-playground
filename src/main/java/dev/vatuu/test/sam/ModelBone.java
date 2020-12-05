package dev.vatuu.test.sam;

import lombok.Getter;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityDestroy;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class ModelBone {

    private boolean isRoot;

    private final Material modelMaterial;
    private final int customModelValue;
    private Location location;

    private List<ModelBone> children = new ArrayList<>();
    private ArmorStand modelArmorStand;

    public ModelBone(Material model, int value, Location loc, boolean isRoot) {
        this.isRoot = isRoot;
        this.modelMaterial = model;
        this.customModelValue = value;
        this.location = loc;
    }

    public ModelBone(Material model, int value, Location loc) {
        this(model, value, loc, false);
    }

    public ModelBone(Location loc) {
        this(Material.AIR, 0, loc, true);
    }

    public void addChild(ModelBone... parts) {
        children.addAll(Arrays.asList(parts));
    }

    public void show(boolean followChildren) {
        if(modelArmorStand == null && !this.isRoot) {
            this.modelArmorStand = this.location.getWorld().spawn(this.location, ArmorStand.class, e -> {
                e.setVisible(false);
                e.setInvulnerable(true);
                e.setGravity(false);
                e.setSilent(true);
                e.getEquipment().setHelmet(getStackCustomModel());
            });
        }

        if(followChildren)
            this.children.forEach(p -> p.show(true));
    }

    public void show() {
        this.show(true);
    }

    public void hide(boolean followChildren) {
        if(this.modelArmorStand != null && !this.isRoot) {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(this.modelArmorStand.getEntityId());
            this.modelArmorStand.getWorld().getPlayers().forEach(p -> ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet));
            this.modelArmorStand.remove();
            this.modelArmorStand = null;
        }

        if(followChildren)
            this.children.forEach(p -> p.hide(true));
    }

    public void hide() {
        this.hide(true);
    }

    public void translate(Vector vec, boolean followChildren) {
        this.location = this.location.add(vec);
        if(this.modelArmorStand != null && !this.isRoot) {
            this.hide(false);
            this.show(false);
        }

        if(followChildren)
            this.children.forEach(p -> p.translate(vec, true));
    }

    public void translate(Vector vec) {
        this.translate(vec, true);
    }

    public void moveTo(Location loc, boolean followChildren) {
        this.location = loc;
        if(modelArmorStand != null && !this.isRoot)
            this.modelArmorStand.teleport(this.location);

        if(followChildren)
            this.children.forEach(p -> p.moveTo(loc, true));
    }

    public void moveTo(Location loc) {
        this.moveTo(loc, true);
    }

    private ItemStack getStackCustomModel()  {
        ItemStack stack = new ItemStack(this.modelMaterial);
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(this.customModelValue);
        stack.setItemMeta(meta);
        return stack;
    }
}
