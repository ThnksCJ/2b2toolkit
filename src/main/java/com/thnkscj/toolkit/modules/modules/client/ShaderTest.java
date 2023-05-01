package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.event.events.render.Render2DEvent;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.util.shader.Shaders;

public class ShaderTest extends Module {

    public ShaderTest(){
        super("ShaderTest", "sus", Category.CLIENT);
    }

    @Override
    public void onRender2D(Render2DEvent event){
        Shaders.blur(4, 1, 1, () -> mc.fontRenderer.drawStringWithShadow("this is a test", 2, 2, 0xffffff));
    }
}
