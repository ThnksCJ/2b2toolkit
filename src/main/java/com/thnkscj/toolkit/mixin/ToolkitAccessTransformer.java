package com.thnkscj.toolkit.mixin;

import net.minecraftforge.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public final class ToolkitAccessTransformer extends AccessTransformer {
    public ToolkitAccessTransformer() throws IOException {
        super("toolkit_at.cfg");
    }
}
