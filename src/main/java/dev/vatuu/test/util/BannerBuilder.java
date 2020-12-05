package dev.vatuu.test.util;

import com.google.common.collect.Lists;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

import java.util.List;

public class BannerBuilder {
    public enum Numeric {
        ZERO(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_TOP, PatternType.STRIPE_RIGHT, PatternType.STRIPE_BOTTOM,
                PatternType.STRIPE_LEFT, PatternType.BORDER),
                Lists.newArrayList(false, true, true, true, true, false)
        ),
        ONE(Lists.newArrayList(
                PatternType.BASE, PatternType.SQUARE_TOP_LEFT, PatternType.TRIANGLES_TOP, PatternType.STRIPE_CENTER,
                PatternType.BORDER),
                Lists.newArrayList(false, true, false, true, false)
        ),
        TWO(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_TOP, PatternType.RHOMBUS_MIDDLE, PatternType.STRIPE_DOWNLEFT,
                PatternType.STRIPE_BOTTOM, PatternType.BORDER),
                Lists.newArrayList(false, true, false, true, true, false)
        ),
        THREE(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_MIDDLE, PatternType.STRIPE_LEFT, PatternType.STRIPE_BOTTOM,
                PatternType.STRIPE_RIGHT, PatternType.STRIPE_TOP, PatternType.BORDER),
                Lists.newArrayList(false, true, false, true, true, true, false)
        ),
        FOUR(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_LEFT, PatternType.HALF_HORIZONTAL_MIRROR, PatternType.STRIPE_RIGHT,
                PatternType.STRIPE_MIDDLE, PatternType.BORDER),
                Lists.newArrayList(false, true, false, true, true, false)
        ),
        FIVE(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_BOTTOM, PatternType.STRIPE_DOWNLEFT, PatternType.CURLY_BORDER,
                PatternType.SQUARE_BOTTOM_LEFT, PatternType.STRIPE_TOP, PatternType.BORDER),
                Lists.newArrayList(false, true, true, false, true, true, false)
        ),
        SIX(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_BOTTOM, PatternType.STRIPE_RIGHT, PatternType.HALF_HORIZONTAL,
                PatternType.STRIPE_MIDDLE, PatternType.STRIPE_TOP, PatternType.STRIPE_LEFT, PatternType.BORDER),
                Lists.newArrayList(false, true, true, false, true, true, true, false)
        ),
        SEVEN(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_TOP, PatternType.DIAGONAL_RIGHT, PatternType.STRIPE_DOWNLEFT,
                PatternType.SQUARE_BOTTOM_LEFT, PatternType.BORDER),
                Lists.newArrayList(false, true, false, true, true, false)
        ),
        EIGHT(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_TOP, PatternType.STRIPE_LEFT, PatternType.STRIPE_MIDDLE,
                PatternType.STRIPE_BOTTOM, PatternType.STRIPE_RIGHT, PatternType.BORDER),
                Lists.newArrayList(false, true, true, true, true, true, false)
        ),
        NINE(Lists.newArrayList(
                PatternType.BASE, PatternType.STRIPE_LEFT, PatternType.HALF_HORIZONTAL_MIRROR, PatternType.STRIPE_MIDDLE,
                PatternType.STRIPE_TOP, PatternType.STRIPE_RIGHT, PatternType.STRIPE_BOTTOM, PatternType.BORDER),
                Lists.newArrayList(false, true, false, true, true, true, true, false));

        private final List<PatternType> patterns;
        private final List<Boolean> isFontPattern;

        Numeric(List<PatternType> patterns, List<Boolean> isFont) {
            this.patterns = patterns;
            this.isFontPattern = isFont;
        }

        public ItemStack getBannerItem(DyeColor base, DyeColor font) {
            ItemStack stack = new ItemStack(Material.WHITE_BANNER, 1);
            BannerMeta meta = (BannerMeta) stack.getItemMeta();
            for (int i = 0; i < patterns.size(); i++)
                meta.addPattern(new Pattern(isFontPattern.get(i) ? font : base, patterns.get(i)));
            stack.setItemMeta(meta);
            return stack;
        }
    }
}
