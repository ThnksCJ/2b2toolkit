package com.cj.toolkit.modules.modules;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.event.events.block.RenderBlockEvent;
import com.cj.toolkit.modules.Category;
import com.cj.toolkit.modules.Module;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PortalInfo extends Module {
    public PortalInfo() {
        super("PortalInfo", "Shows portal info", Category.CLIENT);
    }

    @SubscribeEvent
    public void onRenderBlock(RenderBlockEvent event) {
        if (event.getBlock().getLocalizedName().equals("Portal")) {
            BlockPos pos = event.getPos();

            if (mc.player.dimension == 1) {
                Command.sendMessage("Overworld: " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
            } else if (mc.player.dimension == 0) {
                Command.sendMessage("Nether: " + pos.getX() + " " + pos.getY() + " " + pos.getZ());
            }
        }
    }
}
