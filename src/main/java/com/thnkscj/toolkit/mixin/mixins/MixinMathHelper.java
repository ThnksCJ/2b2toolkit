package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.mixin.Precedence;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.client.AntiLeak;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MathHelper.class, priority = Precedence.MAXIMUM)
public class MixinMathHelper {
    @Inject(method = "getCoordinateRandom", at = @At("HEAD"), cancellable = true)
    private static void modifyRandomTexture(int x, int y, int z, CallbackInfoReturnable<Long> cir) {
        if (AntiLeak.textureRot.getValue() && ModuleManager.getModule(AntiLeak.class).isEnabled()) {
            int newX = AntiLeak.offsetRand.getX();
            int newY = AntiLeak.offsetRand.getY();
            int newZ = AntiLeak.offsetRand.getZ();

            long i = (newX * 3129871L) ^ (long) newZ * 116129781L ^ (long) newY;
            i = i * i * 42317861L + i * 11L;

            cir.setReturnValue(i);
        }
    }
}
