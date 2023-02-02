package com.cj.toolkit.modules.modules;

import com.cj.toolkit.modules.Category;
import com.cj.toolkit.modules.Module;
import com.cj.toolkit.setting.settings.IntegerSetting;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public class AntiElytraCrash extends Module {
    public AntiElytraCrash() {
        super("AntiElytraCrash", "Attempts to prevent elytra crash. Idea from IronException#4092", Category.CLIENT);

        addSettings(crashDistance);
    }

    public static IntegerSetting crashDistance = new IntegerSetting("Crash Distance", "Distance to stop", 1, 5, 15);

    @Override
    public void onUpdate() {
        if(mc.player.isElytraFlying()) {
            if(isPlayerAboutToCollide(mc.player)) {
                mc.player.motionX = 0;
                mc.player.motionY = 0;
                mc.player.motionZ = 0;

                /* tbh this might work, ill see when i got prio
                mc.player.posX -= mc.player.motionX * crashDistance.getValue();
                mc.player.posY -= mc.player.motionY * crashDistance.getValue();
                mc.player.posZ -= mc.player.motionZ * crashDistance.getValue();
                 */
            }
        }
    }

    private boolean isPlayerAboutToCollide(EntityPlayer player) {
        double playerX = player.posX;
        double playerY = player.posY;
        double playerZ = player.posZ;

        double playerMotionX = player.motionX * crashDistance.getValue();
        double playerMotionY = player.motionY * crashDistance.getValue();
        double playerMotionZ = player.motionZ * crashDistance.getValue();

        int blockX = MathHelper.floor(playerX + playerMotionX);
        int blockY = MathHelper.floor(playerY + playerMotionY);
        int blockZ = MathHelper.floor(playerZ + playerMotionZ);

        BlockPos blockPos = new BlockPos(blockX, blockY, blockZ);
        IBlockState blockState = player.world.getBlockState(blockPos);

        return !blockState.getBlock().getLocalizedName().equals("Air");
    }
}
