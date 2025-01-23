package net.corespring.csaugmentations.DataGen;

import net.corespring.csaugmentations.DataGen.loot.CSBlockLootTables;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class CSLootTableProvider {
    public static LootTableProvider create(PackOutput output) {
        return new LootTableProvider(output, Set.of(), List.of(
                new LootTableProvider.SubProviderEntry(CSBlockLootTables::new, LootContextParamSets.BLOCK)));
    }
}
