package com.thnkscj.toolkit.event.events.block;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerDestroyBlockEvent extends Event {
    public BlockPos pos;

    public PlayerDestroyBlockEvent(BlockPos pos) {
        this.pos = pos;
    }
}
