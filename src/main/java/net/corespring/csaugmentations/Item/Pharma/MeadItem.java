package net.corespring.csaugmentations.Item.Pharma;

import net.corespring.csaugmentations.Item.AlcoholicBeverageItem;
import net.corespring.cslibrary.Registry.CSItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MeadItem extends AlcoholicBeverageItem {

    public MeadItem(int tier, Properties pProperties) {
        super(tier, pProperties);
    }

    @Override
    protected MobEffectInstance[] getPositiveEffects() {
        return new MobEffectInstance[]{};
    }

    @NotNull
    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        applyIntoxicationEffect(pLivingEntity);
        applyPositiveEffects(pLivingEntity);

        if (pLivingEntity.hasEffect(MobEffects.POISON)) {
            pLivingEntity.removeEffect(MobEffects.POISON);
        }

        if (pLivingEntity instanceof Player) {
            ((Player) pLivingEntity).getFoodData().eat(this, pStack);
        }

        if (pStack.isEmpty()) {
            return new ItemStack(CSItems.ALCOHOL_BOTTLE.get());
        } else {
            if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild) {
                handleEmptyBottle(player);
                pStack.shrink(1);
            }
        }
        return pStack;
    }

    @Override
    protected int getBaseDurationPerDrink() {
        return 1000 * getTier();
    }

    @Override
    protected int getBaseAmplifierPerDrink() {
        return 1;
    }
}
