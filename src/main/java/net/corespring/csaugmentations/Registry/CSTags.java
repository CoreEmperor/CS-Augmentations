package net.corespring.csaugmentations.Registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CSTags {
    public CSTags() {
    }

    private static TagKey<Item> tag(String name) {
        return ItemTags.create(new ResourceLocation("csaugmentations", name));
    }

    public static class Items {
        public static final TagKey<Item> MORTARS = tag("mortars");
        public static final TagKey<Item> PESTLES = tag("pestles");
        public static final TagKey<Item> BLOOD_FILTERS = tag("blood_filters");
        public static final TagKey<Item> RAW_MEATS = tag("raw_meats");

        public Items() {
        }
    }
}