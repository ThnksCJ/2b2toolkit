package com.thnkscj.toolkit.modules.modules;

import com.thnkscj.toolkit.event.events.render.Render2DEvent;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.util.render.RenderUtil;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class PortalInfo extends Module {
    public PortalInfo() {
        super("Portal Info", "shows info about portals", Category.CLIENT);
    }

    @Override
    public void onRender2D(Render2DEvent event) {
        BlockPos block = mc.objectMouseOver.getBlockPos();

        if (Objects.requireNonNull(mc.world.getBlockState(block).getBlock().getRegistryName()).toString().contains("portal")) {
            RenderUtil.drawRectangleBordered(
                    350 + 50,
                    300 - 10,
                    200 + 50,
                    250 - 10,
                    2, 0xFF505060, 0xFF000000);

            mc.fontRenderer.drawString("Portal", 260, 245, 0xFFFFFF);
            mc.fontRenderer.drawString("______", 260, 247, 0xFFFFFF);

            if (mc.world.provider.getDimension() == 0) {
                mc.fontRenderer.drawString("OW Cords: " + block.getX() + " " + block.getY() + " " + block.getZ(), 260, 262, 0xFFFFFF);
                mc.fontRenderer.drawString("Nether Cords: " + block.getX() / 8 + " " + block.getY() / 8 + " " + block.getZ() / 8, 260, 275, 0xFFFFFF);
            }

            if (mc.world.provider.getDimension() == -1) {
                mc.fontRenderer.drawString("Nether Cords: " + block.getX() + " " + block.getY() + " " + block.getZ(), 260, 262, 0xFFFFFF);
                mc.fontRenderer.drawString("OW Cords: " + block.getX() * 8 + " " + block.getY() * 8 + " " + block.getZ() * 8, 260, 275, 0xFFFFFF);
            }
        }
    }
}
