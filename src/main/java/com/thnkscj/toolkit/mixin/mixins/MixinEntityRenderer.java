package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.client.Clans;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityRenderer.class)
public class MixinEntityRenderer {

    @Inject(method = "drawNameplate", at = @At(value = "HEAD"), cancellable = true)
    private static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo ci) {
        if (ModuleManager.getModule(Clans.class).isEnabled()) {
            ci.cancel();

            Clans.drawNameplate(fontRendererIn, str, x, y, z, verticalShift, viewerYaw, viewerPitch, isThirdPersonFrontal, isSneaking);
        }
    }
}
