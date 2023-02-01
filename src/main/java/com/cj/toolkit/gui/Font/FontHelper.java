package com.cj.toolkit.gui.Font;

import net.minecraft.client.Minecraft;

public class FontHelper {
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static float drawShadowedString(String string, int x, int y, int color) {
        return mc.fontRenderer.drawStringWithShadow(string, x, y, color);
    }

    public static int getStringWidth(String str) {
        return mc.fontRenderer.getStringWidth(str);
    }

    public static int getFontHeight() {
        return mc.fontRenderer.FONT_HEIGHT;
    }

    public static float drawKeyStringWithShadow(String string, int x, int y, int color) {
        return mc.fontRenderer.drawStringWithShadow(string, x, y, color);
    }

}
