package com.thnkscj.toolkit.gui.click.components.settings;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.Gui;
import com.thnkscj.toolkit.gui.click.components.Button;
import com.thnkscj.toolkit.setting.settings.BooleanSetting;
import com.thnkscj.toolkit.util.misc.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public class CheckBox extends com.thnkscj.toolkit.gui.click.components.Component {
    private boolean hovered;
    private final BooleanSetting op;
    private int x;
    private int y;
    private final Button parent;
    private int offset;
    private float hoveredWidth = 1;
    private int alpha;

    public CheckBox(BooleanSetting booleanSetting, Button button, int n) {
        op = booleanSetting;
        parent = button;
        x = button.parent.getX() + button.parent.getWidth();
        y = button.parent.getY() + button.offset;
        offset = n;
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (isMouseOnButton(n, n2) && n3 == 0 && parent.open) {
            op.toggle();
            Minecraft.getMinecraft().world.playSound(Minecraft.getMinecraft().player, Minecraft.getMinecraft().player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1, 1);
        }
    }

    @Override
    public int getHeight() {
        return 15;
    }

    @Override
    public void setOff(int n) {
        offset = n;
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > x && n < x + 108 && n2 > y && n2 < y + 15;
    }

    @Override
    public void updateComponent(int n, int n2) {
        if (!this.parent.open) {
            alpha = 0;
        }
        hovered = isMouseOnButton(n, n2);
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

        if (hovered) {
            if (hoveredWidth < 3) {
                hoveredWidth += 0.2;
            }
        } else if (hoveredWidth > 1) {
            hoveredWidth -= 0.2;
        }
    }

    @Override
    public void renderComponent() {
        if (op.isEnabled() && alpha < 255) {
            alpha += 15.9375f;
        } else if (!op.isEnabled() && alpha > 0) {
            alpha -= 15.9375f;
        }

        GUIUtils.drawRectangle(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getWidth(), 15, Gui.color2);

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GUIUtils.renderer.drawString(op.getName(), parent.parent.getX() * 2 + 10, (parent.parent.getY() + offset + 2) * 2 - 2, new Color(255, 255, 255, 255).getRGB(), true);
        GL11.glPopMatrix();

        GUIUtils.drawRectangle(parent.parent.getX() + 85, parent.parent.getY() + offset + 3, 9, 9, new Color(183, 183, 183, 236).getRGB());

        GUIUtils.drawRectangle(parent.parent.getX() + 86, parent.parent.getY() + offset + 4, 7, 7, new Color(0, 0, 0).getRGB());

        GUIUtils.drawRectangle(parent.parent.getX() + 86.5f, parent.parent.getY() + offset + 4.5f, 6, 6, new Color(176, 176, 176).getRGB());

        GUIUtils.drawRectangle(parent.parent.getX() + 86.5f, parent.parent.getY() + offset + 4.5f, 6, 6, ColorUtil.changeAlpha(Gui.color, alpha));


    }

    @Override
    public float getHoveredWidth() {
        return hoveredWidth;
    }

}
