package com.thnkscj.toolkit.modules.modules.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.thnkscj.toolkit.modules.HudModule;

public class Test extends HudModule {
    public Test() {
        super(HudCorner.TOP_LEFT, "Watermark");
    }

    @Override
    public void onRender(float partialTicks) {
        super.onRender(partialTicks);
        drawString("[" + ChatFormatting.RED + "Donhack" + ChatFormatting.GOLD + "+" + ChatFormatting.RESET + "] ", 0, 0, -1, true);
    }
}
