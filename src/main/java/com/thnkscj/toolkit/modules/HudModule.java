package com.thnkscj.toolkit.modules;

import java.util.ArrayList;

public class HudModule extends Module {
    public static ArrayList<HudModule> components = new ArrayList<>();
    public HudCorner corner;
    public int xAdd, yAdd;
    public String name;
    public boolean applyScaling = true;
    public ArrayList<HudPoint> renderedPoints = new ArrayList<>();

    public HudModule(HudCorner defaultCorner, String name) {
        super(name, "", Category.HUD);
        this.corner = defaultCorner;
        this.name = name;

        components.add(this);
    }

    public void onRender(float partialTicks) {
        renderedPoints.clear();
    }

    public boolean shouldRender() {
        return this.isEnabled();
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        int displayWidth = (int)(mc.displayWidth / (1 + mc.gameSettings.guiScale * 0.5F));
        int displayHeight = (int)(mc.displayHeight / (1 + mc.gameSettings.guiScale * 0.5F));

        if (corner == HudCorner.BOTTOM_RIGHT) {
            drawString2(text, (displayWidth - 1 - mc.fontRenderer.getStringWidth(text)) + x, (displayHeight - 9) + y, color, shadow);
        } else if (corner == HudCorner.BOTTOM_LEFT) {
            drawString2(text, x + 1, (displayHeight - 9) + y, color, shadow);
        } else if (corner == HudCorner.TOP_LEFT) {
            drawString2(text, x + 1, y + 1, color, shadow);
        } else if (corner == HudCorner.TOP_RIGHT) {
            drawString2(text, displayWidth - mc.fontRenderer.getStringWidth(text) - 1, y + 1, color, shadow);
        } else if (corner == HudCorner.NONE) {
            drawString2(text, x, y, color, shadow);
        }
    }

    private void drawString2(String text, float x, float y, int color, boolean shadow) {
        if (shadow) {
            mc.fontRenderer.drawStringWithShadow(text, x + xAdd, y + yAdd, color);
        } else {
            mc.fontRenderer.drawString(text, x + xAdd, y + yAdd, color, false);
        }

        renderedPoints.add(new HudPoint(x + xAdd, y + yAdd, x + xAdd + mc.fontRenderer.getStringWidth(text), y + yAdd + mc.fontRenderer.FONT_HEIGHT));
    }

    //Turn double into one decimal string
    public static String decimal(double d, int decimal) {
        String s = Double.toString(d);
        try {
            return s.substring(0, s.indexOf(".") + 1 + decimal);
        } catch (IndexOutOfBoundsException e) {
            return s.substring(0, s.indexOf(".") + 1);
        }
    }

    public enum HudCorner {
        TOP_RIGHT(0),
        TOP_LEFT(1),
        BOTTOM_RIGHT(2),
        BOTTOM_LEFT(3),
        NONE(4);

        public int id;
        HudCorner(int id) {
            this.id = id;
        }

        public static HudCorner getCornerFromId(int id) {
            for (HudCorner corner : HudCorner.values()) {
                if (corner.id == id) {
                    return corner;
                }
            }

            return null;
        }
    }

    public class HudPoint {
        public float x, y, x2, y2;

        public HudPoint(float x, float y, float x2, float y2) {
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
        }
    }
}
