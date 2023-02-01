package com.cj.toolkit.event.events.block;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class DamageBlockEvent extends Event {

    private final BlockPos posblock;
    private EnumFacing Direction;

    public DamageBlockEvent(BlockPos blockPos, EnumFacing enumFacing) {
        posblock = blockPos;
        setDirection(enumFacing);
    }

    public BlockPos getPos() {
        return posblock;
    }

    public EnumFacing getDirection() {
        return Direction;
    }

    public void setDirection(EnumFacing direction) {
        Direction = direction;
    }

}