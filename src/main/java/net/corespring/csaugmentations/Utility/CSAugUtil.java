package net.corespring.csaugmentations.Utility;

import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.CSCommonConfigs;
import net.corespring.csaugmentations.Capability.OrganCap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class CSAugUtil {
    public static boolean armsEnabled = true;
    public static boolean legsEnabled = true;

    public static boolean shouldDropOrgan(ItemStack organStack) {
        return organStack.getItem() instanceof SimpleOrgan organ &&
                !isNaturalOrgan(organ);
    }

    public static boolean isNaturalOrgan(SimpleOrgan organ) {
        return organ.getTierName().equalsIgnoreCase(CSOrganTiers.NATURAL.name());
    }

    public static class OrganSlots {
        public static final int BRAIN = 0;
        public static final int EYES = 1;
        public static final int RIBS = 2;
        public static final int SPINE = 3;
        public static final int LEFT_ARM = 4;
        public static final int RIGHT_ARM = 5;
        public static final int LEFT_LEG = 6;
        public static final int RIGHT_LEG = 7;
        public static final int LUNGS = 8;
        public static final int LEFT_KIDNEY = 9;
        public static final int RIGHT_KIDNEY = 10;
        public static final int LIVER = 11;
        public static final int HEART = 12;
        public static final int STOMACH = 13;
        public static final int SKIN = 14;
        public static final int POWER_SOURCE = 15;
    }
}
