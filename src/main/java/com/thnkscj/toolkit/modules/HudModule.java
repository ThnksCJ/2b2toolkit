package com.thnkscj.toolkit.modules;

import java.util.ArrayList;

public class HudModule extends Module {
    public static ArrayList<HudModule> components = new ArrayList<>();
    public HudCorner corner;
    public int xPos, yPos;
    public float scale = 1F;
    public boolean isSelected = false;
    public ArrayList<HudPoint> renderedPoints = new ArrayList<>();

    public HudModule(String name, String description, HudCorner defaultCorner) {
        super(name, description, Category.HUD);
        this.corner = defaultCorner;

        components.add(this);
    }

    public void onRender(float partialTicks) {
        renderedPoints.clear();
    }

    public boolean shouldRender() {
        return this.isEnabled();
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        int displayWidth = (int) (mc.displayWidth / (1 + mc.gameSettings.guiScale * 0.5F));
        int displayHeight = (int) (mc.displayHeight / (1 + mc.gameSettings.guiScale * 0.5F));

        if (corner == HudCorner.BOTTOM_RIGHT) {
            drawString2(text, (displayWidth - 1 - mc.fontRenderer.getStringWidth(text)) + x, (displayHeight - 9) + y, color, shadow);
        } else if (corner == HudCorner.BOTTOM_LEFT) {
            drawString2(text, x + 1, (displayHeight - 9) + y, color, shadow);
        } else if (corner == HudCorner.TOP_LEFT) {
            drawString2(text, x + 1, y + 1, color, shadow);
        } else if (corner == HudCorner.TOP_RIGHT) {
            drawString2(text, displayWidth - mc.fontRenderer.getStringWidth(text) - 1, y + 1, color, shadow);
        } else if (corner == HudCorner.CENTER) {
            drawString2(text, ((float) displayWidth / 2 - (float) mc.fontRenderer.getStringWidth(text) / 2) + x, ((float) displayHeight / 2 - (float) mc.fontRenderer.FONT_HEIGHT / 2) + y, color, shadow);
        } else if (corner == HudCorner.NONE) {
            drawString2(text, x, y, color, shadow);
        }
    }

    private void drawString2(String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            mc.fontRenderer.drawStringWithShadow(text, x + xPos, y + yPos, color);
        } else {
            mc.fontRenderer.drawString(text, x + xPos, y + yPos, color, false);
        }

        renderedPoints.add(new HudPoint(x + xPos, y + yPos, x + xPos + mc.fontRenderer.getStringWidth(text), y + yPos + mc.fontRenderer.FONT_HEIGHT));
    }

    public enum HudCorner {
        TOP_RIGHT(),
        TOP_LEFT(),
        BOTTOM_RIGHT(),
        BOTTOM_LEFT(),
        CENTER(),
        NONE()
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
