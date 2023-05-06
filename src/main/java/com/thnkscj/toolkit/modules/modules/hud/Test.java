package com.thnkscj.toolkit.modules.modules.hud;

import com.thnkscj.toolkit.modules.HudModule;

import java.awt.*;

public class Test extends HudModule {
    public Test() {
        super("Watermark", "");
    }

    @Override
    public void onRender(float partialTicks) {
        super.onRender(partialTicks);

        drawShaderRect(0, 0, 200, 200, 10, 1, new Color(0, 0, 0, 0), new Color(0, 255, 255));
    }
}
