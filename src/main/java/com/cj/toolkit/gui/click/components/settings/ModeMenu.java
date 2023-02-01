package com.cj.toolkit.gui.click.components.settings;

import com.cj.toolkit.gui.GUIUtils;
import com.cj.toolkit.gui.click.Gui;
import com.cj.toolkit.gui.click.components.Button;
import com.cj.toolkit.gui.click.components.Component;
import com.cj.toolkit.modules.ModuleManager;
import com.cj.toolkit.modules.modules.ClickGui;
import com.cj.toolkit.setting.settings.EnumSetting;
import com.cj.toolkit.util.misc.ColorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class ModeMenu extends Component {

    private boolean hovered;
    private final EnumSetting op;
    private int x;
    private int y;
    private final Button parent;
    private int offset;
    private float hoveredWidth = 1;
    public ArrayList subcomponents;
    private Enum selected;
    private int selectedY = 17;
    private int smoothY = 17;
    private int alpha = 150;
    private int scissor = 0;
    private int scissor2 = 0;
    private int rotation;


    public ModeMenu(EnumSetting enumSetting, Button button, int n) {
        op = enumSetting;
        parent = button;
        x = button.parent.getX() + button.parent.getWidth();
        y = button.parent.getY() + button.offset;
        offset = n;
        this.selected = getSelected();
        this.subcomponents = new ArrayList();
        for (Enum e : enumSetting.getValues()) {
            subcomponents.add(e);
        }
        rotation = 0;

    }


    public Enum getSelected() {
        for (Enum enums : op.getValues()) {
            if (enums.toString() == op.getValueName())
                return enums;
        }
        return null;
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 1 && this.parent.open) {
            this.opened = !this.opened;
            parent.parent.refresh();

        }
        if (n3 == 0) {
            if (this.opened) {
                int addY = 17;
                for (Object object : this.subcomponents) {
                    Enum e = (Enum) object;
                    if (isMouseOnEnum(n, n2, addY)) {
                        if (addY != selectedY) {
                            selected = e;
                            selectedY = addY;
                            addY += 15;
                            op.setValue(selected);
                            if (Objects.equals(this.parent.mod.getName(), "ClickGui")) {
                                GUIUtils.updateFont();
                                if (Objects.equals(this.op.getName(), "BackgroundMode")) {
                                    ModuleManager.getModule(ClickGui.class).updateShader();
                                }
                            }
                        }
                    }
                    addY += 15;
                }


            }
        }

    }

    @Override
    public void setOff(int n) {
        offset = n;
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > x && n < x + 108 && n2 > y && n2 < y + 15;
    }

    public boolean isMouseOnEnum(int n, int n2, int offset) {
        return n > this.x && n < this.x + 88 && n2 > this.y + offset && n2 < this.y + 16 + offset;
    }

    @Override
    public void updateComponent(int n, int n2) {
        hovered = isMouseOnButton(n, n2);
        y = parent.parent.getY() + offset;
        x = parent.parent.getX();

        if (!this.parent.open) {
            rotation = 0;
        }

        if (this.opened) {
            int addY = 17;
            for (Object object : this.subcomponents) {
                Enum e = (Enum) object;
                if (Objects.equals(e.toString(), selected.toString())) {
                    selectedY = addY;
                }
                addY += 15;
            }
        }

        if (smoothY != selectedY && alpha > 150) {
            alpha -= 25;

        }

        if (smoothY < selectedY) {
            smoothY += 1;
        } else if (smoothY > selectedY) {
            smoothY -= 1;
        } else {
            if (alpha < 250) {
                alpha += 25;
            }
        }

        if (hovered) {
            if (hoveredWidth < 3) {
                hoveredWidth += 0.2;
            }
        } else if (hoveredWidth > 1) {
            hoveredWidth -= 0.2;
        }

        if (this.opened) {
            if (this.subcomponents.size() == 2) {
                if (scissor < this.getHeight() - 11) {
                    scissor += 1;
                }
            } else {
                if (scissor < this.getHeight() - 21) {
                    scissor += 1;
                }
            }
        } else {
            scissor = 0;
        }

        if (this.opened) {
            if (scissor2 < this.getHeight() - 21) {
                scissor2 += 2;
            }
        } else {
            scissor2 = 0;
        }

        if (scissor2 - this.getHeight() - 21 < 3 || this.getHeight() - scissor2 - 21 < 3) {
            scissor2 = this.getHeight() - 21;
        }

        if (!opened) {
            if (rotation > 90) {
                rotation -= 2;
            } else if (rotation < 90) {
                rotation += 2;
            }

        } else {
            if (rotation < 180) {
                rotation += 2;
            }
        }
    }

    @Override
    public void renderComponent() {

        //GUIUtils.drawTriangle(parent.parent.getX() + 80, parent.parent.getY() + offset + 7, 20, 90, new Color(255,255,255).getRGB());

        GUIUtils.drawRectangle(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getWidth(), this.getHeight(), Gui.color2);

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GUIUtils.renderer.drawString(op.getName(), parent.parent.getX() * 2 + 10, (parent.parent.getY() + offset + 2) * 2 - 2, new Color(255, 255, 255, 255).getRGB(), true);

        GL11.glPopMatrix();
        if (this.opened) {

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GUIUtils.glScissor(this.parent.parent.getX() + 9, this.parent.parent.getY() + this.offset + 14, this.parent.parent.getX() + this.parent.parent.getWidth() - 9, this.parent.parent.getY() + this.offset + scissor2 + 16, new ScaledResolution(Minecraft.getMinecraft()));
            GUIUtils.circle(parent.parent.getX() + parent.parent.getWidth() / 2, this.parent.parent.getY() + this.offset, scissor + 27, Gui.color);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GUIUtils.glScissor(this.parent.parent.getX() + 10, this.parent.parent.getY() + this.offset + 15, this.parent.parent.getX() + this.parent.parent.getWidth() - 10, this.parent.parent.getY() + this.offset + scissor2 + 15, new ScaledResolution(Minecraft.getMinecraft()));
            GUIUtils.circle(parent.parent.getX() + parent.parent.getWidth() / 2, this.parent.parent.getY() + this.offset, scissor + 26, new Color(43, 46, 51, 255).getRGB());
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GUIUtils.glScissor(this.parent.parent.getX() + 10, this.parent.parent.getY() + this.offset + 15, this.parent.parent.getX() + this.parent.parent.getWidth() - 10, this.parent.parent.getY() + this.offset + scissor + 15, new ScaledResolution(Minecraft.getMinecraft()));
            GUIUtils.drawRectangle(parent.parent.getX() + 10, parent.parent.getY() + offset + smoothY, parent.parent.getWidth() - 20, 15, ColorUtil.changeAlpha(Gui.color, alpha));

            int addY = 17;
            for (Object object : this.subcomponents) {
                Enum e = (Enum) object;
                GL11.glPushMatrix();
                GL11.glScalef(0.5f, 0.5f, 0.5f);
                GUIUtils.renderer.drawString(e.toString(), parent.parent.getX() * 2 + 35, (parent.parent.getY() + offset + 2) * 2 - 2 + addY * 2, new Color(255, 255, 255, 255).getRGB(), true);
                GL11.glPopMatrix();
                addY += 15;
            }

            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
        }
        GUIUtils.drawTriangle(parent.parent.getX() + 90, parent.parent.getY() + offset + 7.5f, 5, rotation, Gui.color);
    }


    @Override
    public float getHoveredWidth() {
        return hoveredWidth;
    }

    @Override
    public int getHeight() {
        if (this.opened) {
            return 15 * (this.subcomponents.size() + 1) + 10;
        }
        return 15;
    }

}
