package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.event.events.block.DamageBlockEvent;
import com.thnkscj.toolkit.event.events.block.PlayerDestroyBlockEvent;
import com.thnkscj.toolkit.event.events.block.ProcessRightClickBlockEvent;
import com.thnkscj.toolkit.mixin.Precedence;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = PlayerControllerMP.class, priority = Precedence.NORMAL)
public class MixinPlayerControllerMP {

    @Inject(method = "onPlayerDestroyBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> info) {
        PlayerDestroyBlockEvent event = new PlayerDestroyBlockEvent(pos);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    @Inject(method = "onPlayerDamageBlock", at = @At("HEAD"), cancellable = true)
    public void onPlayerDamageBlock(final BlockPos posBlock, final EnumFacing directionFacing, final CallbackInfoReturnable<Boolean> callback) {
        final DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        MinecraftForge.EVENT_BUS.post(event);
    }

    @Inject(method = "processRightClickBlock", at = @At("HEAD"), cancellable = true)
    public void processRightClickBlock(EntityPlayerSP player, WorldClient world, BlockPos pos, EnumFacing facing, Vec3d vec, EnumHand hand, CallbackInfoReturnable<EnumActionResult> info) {
        ProcessRightClickBlockEvent event = new ProcessRightClickBlockEvent(player, world, pos, facing, vec, hand);
        MinecraftForge.EVENT_BUS.post(event);

        if (event.isCanceled()) {
            info.setReturnValue(EnumActionResult.SUCCESS);
            info.cancel();
        }
    }
}
