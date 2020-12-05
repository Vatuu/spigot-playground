package dev.vatuu.test.sam;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public abstract class AnimatableModel {

    public abstract ModelBone getRootBone();

    public void spawn() {
        getRootBone().show();
    }

    public void hide() {
        getRootBone().hide();
    }

    public void translate(Vector vec) {
        getRootBone().translate(vec);
    }

    public void moveTo(Location loc) {
        getRootBone().moveTo(loc);
    }
}
