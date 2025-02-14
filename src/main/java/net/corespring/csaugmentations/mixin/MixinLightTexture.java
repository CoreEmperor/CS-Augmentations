package net.corespring.csaugmentations.mixin;

import net.corespring.csaugmentations.Utility.CSAugUtil;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.DynamicTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LightTexture.class)
public class MixinLightTexture {
    @Shadow
    private DynamicTexture lightTexture;

    @Inject(method = "updateLightTexture", at = @At("HEAD"), cancellable = true)
    private void onUpdateLightTexture(float pPartialTicks, CallbackInfo ci) {
        if (CSAugUtil.nvgEnabled) {
            if (lightTexture != null) {

                for (int i = 0; i < 16 * 16; i++) {
                    lightTexture.getPixels().setPixelRGBA(i % 16, i / 16, 0xFFFFFFFF);
                }
                lightTexture.upload();
            }
            ci.cancel();
        }
    }
}
