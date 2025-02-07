package net.corespring.csaugmentations.Augmentations.Organs.Prosthetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleHeart;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProstheticHeart extends SimpleHeart {
    public ProstheticHeart(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 8;
    }

    @Override
    public void organTick(ItemStack pStack, Level pLevel, Player pPlayer, int pSlotId) {
        if (pPlayer.getHealth() < 10.0) {
            pPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false, true));
            pPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false, true));
        }
    }
}

