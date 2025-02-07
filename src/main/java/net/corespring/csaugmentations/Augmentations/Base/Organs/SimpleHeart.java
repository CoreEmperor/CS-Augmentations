package net.corespring.csaugmentations.Augmentations.Base.Organs;

import net.corespring.csaugmentations.Augmentations.Base.SimpleOrgan;
import net.corespring.csaugmentations.Utility.CSOrganTiers;
import net.corespring.csaugmentations.Utility.IOrganTiers;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class SimpleHeart extends SimpleOrgan {
    public SimpleHeart(IOrganTiers pTier, Properties pProperties) {
        super(pTier, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        double healthBonus = getDoubleAttribute(CSOrganTiers.Attribute.HEALTH);
        pTooltipComponents.add(Component.translatable("")
                .append("" + healthBonus)
                .append(Component.translatable("tooltip.csaugmentations.health"))
                .withStyle(ChatFormatting.BLUE));
    }

    @Override
    public void applyEffects(Player pPlayer) {
        double tier = getTier().getHealth();
        pPlayer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(tier);

    }
}
