package dev.vatuu.test.sam.impl;

import dev.vatuu.test.sam.AnimatableModel;
import dev.vatuu.test.sam.ModelBone;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;

@Getter
public class Player1pxHeadModel extends AnimatableModel {

    private final ModelBone root;

    private final ModelBone head;
    private final ModelBone eyeLeft, eyeRight;
    private final ModelBone eyebrowLeft, eyebrowRight;
    private final ModelBone eyelidLeftUpper, eyelidLeftLower, eyelidRightUpper, eyelidRightLower;

    public Player1pxHeadModel(Location loc, int partCount, int offset, int starting) {
        this.root = new ModelBone(loc);

        this.head = new ModelBone(Material.NAME_TAG, 1 + (partCount * offset) + starting, loc);
        this.root.addChild(head);
        this.eyeLeft = new ModelBone(Material.NAME_TAG, 2 + (partCount * offset) + starting, loc);
        this.eyeRight = new ModelBone(Material.NAME_TAG, 3 + (partCount * offset) + starting, loc);
        this.head.addChild(eyeLeft, eyeRight);
        this.eyelidLeftUpper = new ModelBone(Material.NAME_TAG, 4 + (partCount * offset) + starting, loc);
        this.eyelidLeftLower = new ModelBone(Material.NAME_TAG, 5 + (partCount * offset) + starting, loc);
        this.eyelidRightUpper = new ModelBone(Material.NAME_TAG, 6 + (partCount * offset) + starting, loc);
        this.eyelidRightLower = new ModelBone(Material.NAME_TAG, 7 + (partCount * offset) + starting, loc);
        this.head.addChild(eyelidLeftUpper, eyelidLeftLower, eyelidRightUpper, eyelidRightLower);
        this.eyebrowLeft = new ModelBone(Material.NAME_TAG, 8 + (partCount * offset) + starting, loc);
        this.eyebrowRight = new ModelBone(Material.NAME_TAG, 9 + (partCount * offset) + starting, loc);
        this.head.addChild(eyebrowLeft, eyebrowRight);
    }

    @Override
    public ModelBone getRootBone() {
        return root;
    }
}
