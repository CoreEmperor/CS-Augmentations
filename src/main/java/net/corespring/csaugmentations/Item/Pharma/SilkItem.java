package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.SyringeGunInjectable;
import net.corespring.csaugmentations.Registry.CSEffects;
import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SilkItem extends SyringeGunInjectable {
    public SilkItem(Item.Properties pProperties) {
        super(pProperties);
    }

    private static final List<MobEffectInstance> EFFECTS = List.of(
            new MobEffectInstance(CSEffects.SILK.get(), 1000, 0, false, false, true)
    );

    @Override
    public List<MobEffectInstance> getEffects() {
        return EFFECTS;
    }
}

