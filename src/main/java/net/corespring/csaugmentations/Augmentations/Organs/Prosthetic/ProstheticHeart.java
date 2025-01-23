package net.corespring.csaugmentations.Augmentations.Organs.Prosthetic;

import net.corespring.csaugmentations.Augmentations.Base.Organs.SimpleHeart;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ProstheticHeart extends SimpleHeart {
    public ProstheticHeart(CSOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public int getOrganValue() {
        return 8;
    }

    @Override
    public void applyEffects(Player pPlayer) {
        double tier = getTier().getHealth();
        pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tier);
    }

    @Override
    public void organTick(ItemStack pStack, Level pLevel, Player pPlayer, int pSlotId) {
        if (pPlayer.getHealth() < 10.0) {
            pPlayer.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 100, 0, false, false, true));
            pPlayer.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0, false, false, true));
        }
    }
}

