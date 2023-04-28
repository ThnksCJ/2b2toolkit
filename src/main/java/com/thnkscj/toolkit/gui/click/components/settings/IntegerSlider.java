package com.thnkscj.toolkit.gui.click.components.settings;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.Gui;
import com.thnkscj.toolkit.gui.click.components.Button;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import com.thnkscj.toolkit.util.misc.ColorUtil;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class IntegerSlider extends com.thnkscj.toolkit.gui.click.components.Component {

    private final IntegerSetting val;
    private final com.thnkscj.toolkit.gui.click.components.Button parent;
    private final int parentWidth;
    private final double smoothRenderWidth = 0;
    private boolean hovered;
    private boolean hovered2;
    private int x;
    private boolean dragging = false;
    private int y;
    private int offset;
    private double renderWidth;
    private int smoothWidth;
    private float hoveredWidth = 1;
    private double oldWidth = 0;

    public IntegerSlider(IntegerSetting integerSetting, Button button, int n) {
        this.val = integerSetting;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = n;
        this.parentWidth = this.parent.parent.getWidth() - 30;
    }

    private static double roundToPlace(double d, int n) {
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(n, RoundingMode.HALF_UP);
        return bigDecimal.doubleValue();
    }

    @Override
    public int getHeight() {
        return 25;
    }

    @Override
    public void updateComponent(int n, int n2) {
        if (!this.parent.open) {
            smoothWidth = 0;
        }
        this.hovered2 = this.isMouseOnButton2(n, n2);
        this.hovered = this.isMouseOnButtonD(n, n2) || this.isMouseOnButtonI(n, n2);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        double d = Math.min(this.parentWidth, Math.max(0, n - this.x));
        double d2 = this.val.getMinimumValue();
        double d3 = this.val.getMaximumValue();
        this.renderWidth = this.parentWidth * (this.val.getValue() - d2) / (d3 - d2);
        if (this.dragging) {
            if (d == 0.0) {
                this.val.setValue((int) this.val.getMinimumValue());
            } else {
                double d4 = IntegerSlider.roundToPlace(d / this.parentWidth * (d3 - d2) + d2, 2);
                this.val.setValue((int) d4);
            }
        }

        if (!dragging) {
            if (oldWidth < renderWidth) {
                oldWidth = renderWidth;
            } else if (oldWidth > renderWidth) {
                oldWidth = oldWidth - 1;
            }
        }

        if (hovered2) {
            if (hoveredWidth < 3) {
                hoveredWidth += 0.25;
            }
        } else if (hoveredWidth > 1) {
            hoveredWidth -= 0.25;
        }


    }


    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.isMouseOnButtonD(n, n2) && n3 == 0 && this.parent.open) {
            this.dragging = true;
            oldWidth = renderWidth;
        }
        if (this.isMouseOnButtonI(n, n2) && n3 == 0 && this.parent.open) {
            this.dragging = true;
            oldWidth = renderWidth;
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        this.dragging = false;
    }

    public boolean isMouseOnButtonD(int n, int n2) {
        return n > this.x && n < this.x + (this.parentWidth / 2 + 1) && n2 > this.y && n2 < this.y + 25;
    }

    public boolean isMouseOnButton2(int n, int n2) {
        return n > x && n < x + parent.parent.getWidth() && n2 > y && n2 < y + 25;
    }

    @Override
    public void setOff(int n) {
        this.offset = n;
    }

    public boolean isMouseOnButtonI(int n, int n2) {
        return n > this.x + this.parentWidth / 2 && n < this.x + this.parentWidth && n2 > this.y && n2 < this.y + 25;
    }


    @Override
    public float getHoveredWidth() {
        return hoveredWidth;
    }

    @Override
    public void renderComponent() {

        GUIUtils.drawRectangle(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getWidth(), 25, Gui.color2);

        int length = String.valueOf(val.getValue()).length();

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GUIUtils.renderer.drawString(val.getName(), parent.parent.getX() * 2 + 10, (parent.parent.getY() + offset + 2) * 2 - 2, new Color(255, 255, 255, 255).getRGB(), true);
        GUIUtils.renderer.drawString(val.getValue().toString(), parent.parent.getX() * 2 + 174 - length * 5, (parent.parent.getY() + offset + 2) * 2 + 19, new Color(255, 255, 255, 255).getRGB(), true);
        GL11.glPopMatrix();

        GUIUtils.drawRectangle(this.parent.parent.getX() + 6, this.parent.parent.getY() + this.offset + 15, this.parent.parent.getWidth() - 30, 4, new Color(43, 46, 51, 255).getRGB());

        if (oldWidth > 0) {
            GUIUtils.drawRectangle(this.parent.parent.getX() + 6, this.parent.parent.getY() + this.offset + 15, (int) oldWidth, 4, ColorUtil.changeAlpha(Gui.color, 50));
        }

        GUIUtils.drawRectangle(this.parent.parent.getX() + 6, this.parent.parent.getY() + this.offset + 15, Math.max(smoothWidth, 0), 4, Gui.color);

        if (smoothWidth < (int) renderWidth) {
            smoothWidth += 1;
        } else if (smoothWidth > (int) renderWidth) {
            smoothWidth -= 1;
        }
    }
}
