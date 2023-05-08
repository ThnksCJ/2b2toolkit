package com.thnkscj.toolkit.event.events.block;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlaceBlockEvent extends Event {

    private final BlockPos pos;

    public PlaceBlockEvent(BlockPos pos) {
        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }
}
