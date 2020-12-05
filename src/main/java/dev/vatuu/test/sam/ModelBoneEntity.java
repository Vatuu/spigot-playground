package dev.vatuu.test.sam;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.World;

public class ModelBoneEntity extends EntityArmorStand {

    public ModelBoneEntity(World world) {
        super(EntityTypes.ARMOR_STAND, world);
    }

    public ModelBoneEntity(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public void setInvisible(boolean flag) { }

    @Override
    public boolean isInvisible() {
        return true;
    }
}
