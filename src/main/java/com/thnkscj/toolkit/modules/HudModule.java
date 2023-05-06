package com.thnkscj.toolkit.modules;

import com.thnkscj.toolkit.util.shader.RoundedUtil;

import java.awt.*;
import java.util.ArrayList;

public class HudModule extends Module {
    public static ArrayList<HudModule> components = new ArrayList<>();
    public int xPos, yPos;
    public float scale = 1F;
    public boolean isSelected = false;
    public ArrayList<HudPoint> renderedPoints = new ArrayList<>();

    public HudModule(String name, String description) {
        super(name, description, Category.HUD);

        components.add(this);
    }

    public void onRender(float partialTicks) {
        renderedPoints.clear();
    }

    public boolean shouldRender() {
        return this.isEnabled();
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            mc.fontRenderer.drawStringWithShadow(text, x + xPos, y + yPos, color);
        } else {
            mc.fontRenderer.drawString(text, x + xPos, y + yPos, color, false);
        }

        renderedPoints.add(new HudPoint(x + xPos, y + yPos, x + xPos + mc.fontRenderer.getStringWidth(text), y + yPos + mc.fontRenderer.FONT_HEIGHT));
    }

    public void drawShaderRect(float x, float y, float x2, float y2, int radius, int thicknes, Color fill, Color outline) {
        RoundedUtil.drawRoundOutline(x + xPos, y + yPos, x2, y2, radius, thicknes, fill, outline);
        renderedPoints.add(new HudPoint(x + xPos - thicknes, y + yPos, x2 + xPos, y2 + yPos));
    }

    public static class HudPoint {
        public float x, y, x2, y2;

        public HudPoint(float x, float y, float x2, float y2) {
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
}
