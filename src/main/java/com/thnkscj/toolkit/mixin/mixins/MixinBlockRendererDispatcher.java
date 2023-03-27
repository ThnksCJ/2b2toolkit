package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.event.events.block.RenderBlockEvent;
import com.thnkscj.toolkit.mixin.Precedence;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.AntiLeak;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockRendererDispatcher.class, priority = Precedence.MAXIMUM)
public class MixinBlockRendererDispatcher {

    @Inject(method = "renderBlock", at = @At("HEAD"), cancellable = true)
    public void renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn, CallbackInfoReturnable<Boolean> info) {
        RenderBlockEvent renderBlockEvent = new RenderBlockEvent(state, pos, blockAccess, bufferBuilderIn);
        MinecraftForge.EVENT_BUS.post(renderBlockEvent);

        if (!AntiLeak.terrainHidden.getValue() || !ModuleManager.getModule(AntiLeak.class).isEnabled()) {
            return;
        }

        if (
                state.getBlock() == Blocks.BEDROCK ||
                        state.getBlock() == Blocks.TALLGRASS ||
                        state.getBlock() == Blocks.DOUBLE_PLANT ||
                        state.getBlock() == Blocks.DEADBUSH ||
                        state.getBlock() == Blocks.RED_FLOWER ||
                        state.getBlock() == Blocks.YELLOW_FLOWER ||
                        state.getBlock() == Blocks.SNOW_LAYER
        ) {

            info.setReturnValue(false);
        }

        if (pos.getY() < AntiLeak.terrainRenderCutoff.getValue()) {
            info.setReturnValue(false);
        }
    }
}
