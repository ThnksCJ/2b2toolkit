package com.cj.toolkit.event.events.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RenderBlockEvent extends Event {
    private final IBlockState state;
    private final BlockPos pos;
    private final IBlockAccess access;
    private final BufferBuilder bufferBuilder;

    public RenderBlockEvent(IBlockState state, BlockPos pos, IBlockAccess access, BufferBuilder bufferBuilder) {
        this.state = state;
        this.pos = pos;
        this.access = access;
        this.bufferBuilder = bufferBuilder;
    }

    public IBlockState getState() {
        return state;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockAccess getAccess() {
        return access;
    }

    public BufferBuilder getBufferBuilder() {
        return bufferBuilder;
    }

    public Block getBlock() {
        return getState().getBlock();
    }
}
