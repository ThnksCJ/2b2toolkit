package com.thnkscj.toolkit.mixin.mixins;

import com.google.common.collect.Lists;
import com.thnkscj.toolkit.modules.modules.client.AntiLeak;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOverlayDebug;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public class MixinGuiOverlayDebug {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Inject(method = "call", at = @At("HEAD"), cancellable = true)
    public void callInject(CallbackInfoReturnable<List<String>> cir){
        if(AntiLeak.fthreeSpoof.getValue() == AntiLeak.F3Spoof.Off)
            return;
        AntiLeak.F3Spoof f3Spoof = AntiLeak.fthreeSpoof.getValue();
        int offX = AntiLeak.offsetRand.getX();
        int offY = AntiLeak.offsetRand.getY();
        int offZ = AntiLeak.offsetRand.getZ();
        BlockPos blockpos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);
        if (this.mc.isReducedDebug()) {
            cir.setReturnValue(Lists.newArrayList("Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ")", this.mc.debug, this.mc.renderGlobal.getDebugInfoRenders(), this.mc.renderGlobal.getDebugInfoEntities(), "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(), this.mc.world.getProviderName(), "", String.format("Chunk-relative: %d %d %d", (blockpos.x + offX) & 15, (blockpos.y + offY) & 15, (blockpos.z + offZ) & 15)));
        } else {
            Entity entity = this.mc.getRenderViewEntity();
            EnumFacing enumfacing = f3Spoof == AntiLeak.F3Spoof.Goofy ? EnumFacing.DOWN : entity.getHorizontalFacing();
            String s = "Invalid";
            if(f3Spoof == AntiLeak.F3Spoof.Compatible){
                switch(enumfacing) {
                    case NORTH:
                        s = "Towards negative Z";
                        break;
                    case SOUTH:
                        s = "Towards positive Z";
                        break;
                    case WEST:
                        s = "Towards negative X";
                        break;
                    case EAST:
                        s = "Towards positive X";
                }
            }
            String coords = String.format("XYZ: %.3f / %.5f / %.3f", (float) Integer.MAX_VALUE, (float) Integer.MIN_VALUE, (float) Integer.MAX_VALUE);
            if(f3Spoof == AntiLeak.F3Spoof.Compatible)
                coords = String.format("XYZ: %.3f / %.5f / %.3f", this.mc.getRenderViewEntity().posX + offX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY + offY, this.mc.getRenderViewEntity().posZ + offZ);
            List<String> list = Lists.newArrayList(
                    "Minecraft 1.12.2 (" + this.mc.getVersion() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.mc.getVersionType()) ? "" : "/" + this.mc.getVersionType()) + ")",
                    this.mc.debug,
                    this.mc.renderGlobal.getDebugInfoRenders(),
                    this.mc.renderGlobal.getDebugInfoEntities(),
                    "P: " + this.mc.effectRenderer.getStatistics() + ". T: " + this.mc.world.getDebugLoadedEntities(),
                    this.mc.world.getProviderName(),
                    "",
                    coords,
                    f3Spoof == AntiLeak.F3Spoof.Goofy ? String.format("Block: %d %d %d", 0, 0, 0) : String.format("Block: %d %d %d", blockpos.getX() + offX, blockpos.getY() + offY, blockpos.getZ() + offY),
                    f3Spoof == AntiLeak.F3Spoof.Goofy ? "Chunk: 0 0 0 in 0 0 0" : String.format("Chunk: %d %d %d in %d %d %d", (blockpos.x + offX) & 15, (blockpos.y + offY) & 15, (blockpos.z + offZ) & 15, (blockpos.x + offX) >> 4, (blockpos.y + offY) >> 4, (blockpos.z + offZ) >> 4),
                    String.format("Facing: %s (%s) (%.1f / %.1f)", enumfacing, s, MathHelper.wrapDegrees(f3Spoof == AntiLeak.F3Spoof.Goofy ? 0f : entity.rotationYaw), MathHelper.wrapDegrees(f3Spoof == AntiLeak.F3Spoof.Goofy ? 0f : entity.rotationPitch)));
            if (this.mc.world != null) {
                Chunk chunk = this.mc.world.getChunk(blockpos);
                if (this.mc.world.isBlockLoaded(blockpos) && blockpos.getY() >= 0 && blockpos.getY() < 256) {
                    if (!chunk.isEmpty()) {
                        if(f3Spoof == AntiLeak.F3Spoof.Goofy) list.add("Biome: " + "Nope");
                        list.add("Light: " + (f3Spoof == AntiLeak.F3Spoof.Goofy ? "Nope" : chunk.getLightSubtracted(blockpos, 0) + " (" + chunk.getLightFor(EnumSkyBlock.SKY, blockpos) + " sky, " + chunk.getLightFor(EnumSkyBlock.BLOCK, blockpos) + " block)"));
                        DifficultyInstance difficultyinstance = this.mc.world.getDifficultyForLocation(blockpos);
                        if (this.mc.isIntegratedServerRunning() && this.mc.getIntegratedServer() != null) {
                            EntityPlayerMP entityplayermp = this.mc.getIntegratedServer().getPlayerList().getPlayerByUUID(this.mc.player.getUniqueID());
                            if (entityplayermp != null) {
                                difficultyinstance = entityplayermp.world.getDifficultyForLocation(new BlockPos(entityplayermp));
                            }
                        }

                        list.add(String.format("Local Difficulty: %.2f // %.2f (Day %d)", difficultyinstance.getAdditionalDifficulty(), difficultyinstance.getClampedAdditionalDifficulty(), this.mc.world.getWorldTime() / 24000L));
                    } else {
                        list.add("Waiting for chunk...");
                    }
                } else {
                    list.add("Outside of world...");
                }
            }

            if (this.mc.entityRenderer != null && this.mc.entityRenderer.isShaderActive()) {
                list.add("Shader: " + this.mc.entityRenderer.getShaderGroup().getShaderGroupName());
            }

            if (this.mc.objectMouseOver != null && this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK && this.mc.objectMouseOver.getBlockPos() != null) {
                BlockPos blockpos1 = this.mc.objectMouseOver.getBlockPos();
                if(f3Spoof == AntiLeak.F3Spoof.Goofy)
                    list.add("Looking at: nothing");
                else
                    list.add(String.format("Looking at: %d %d %d", blockpos1.getX() + offX, blockpos1.getY() + offY, blockpos1.getZ() + offZ));
            }

            cir.setReturnValue(list);
        }
    }
}
