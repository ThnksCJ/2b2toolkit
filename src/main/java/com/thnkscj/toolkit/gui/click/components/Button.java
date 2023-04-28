package com.thnkscj.toolkit.gui.click.components;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.Gui;
import com.thnkscj.toolkit.gui.click.components.settings.*;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.setting.Setting;
import com.thnkscj.toolkit.setting.settings.*;
import com.thnkscj.toolkit.util.misc.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

import static com.thnkscj.toolkit.util.Wrapper.mc;

public class Button extends Component {
    private final int height;
    private final int alpha = 0;
    public int inUp = 0;
    public boolean open;
    public int offset;
    public Module mod;
    public Frame parent;
    public boolean isHovered;
    public ArrayList subcomponents;
    int mouseX;
    int mouseY;
    Timer timer = new Timer();
    private float anim = 0;
    private int openAnim = 0;
    private boolean pickingKey;
    private float hoveredWidth = 1;
    private boolean hovered2;


    public Button(Module module, Frame frame, int n) {
        Component component;
        this.mod = module;
        this.parent = frame;
        this.offset = n + 1;
        this.subcomponents = new ArrayList();
        this.open = false;
        this.pickingKey = false;
        this.height = 15;
        int n2 = n + 15;
        for (Object obj : module.getSettings()) {
            Setting setting = (Setting) obj;
            if (setting instanceof BooleanSetting) {
                BooleanSetting set = (BooleanSetting) setting;
                CheckBox checkbox = new CheckBox(set, this, n2);
                subcomponents.add(checkbox);
                n2 += 15;
            } else if (setting instanceof IntegerSetting) {
                IntegerSetting set = (IntegerSetting) setting;
                IntegerSlider slider = new IntegerSlider(set, this, n2);
                subcomponents.add(slider);
                n2 += 15;
            } else if (setting instanceof DoubleSetting) {
                DoubleSetting set = (DoubleSetting) setting;
                DoubleSlider slider = new DoubleSlider(set, this, n2);
                subcomponents.add(slider);
                n2 += 15;
            } else if (setting instanceof EnumSetting) {
                EnumSetting set = (EnumSetting) setting;
                ModeMenu menu = new ModeMenu(set, this, n2);
                subcomponents.add(menu);
                n2 += 15;
            } else if (setting instanceof ColorSetting) {
                ColorSetting set = (ColorSetting) setting;
                ColorPicker picker = new ColorPicker(set, this, n2);
                subcomponents.add(picker);
                n2 += 15;
            }
        }
    }

    @Override
    public void setOff(int n) {
        this.offset = n;
        int n2 = this.offset + 30;
        for (Object componentw : this.subcomponents) {
            Component component = (Component) componentw;
            component.setOff(n2);
            n2 += component.getHeight();

        }
    }

    @Override
    public void renderComponent() {
        ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
        if (this.mod.isEnabled() && openAnim < 100) {
            openAnim += 4;
        } else if (!this.mod.isEnabled() && openAnim > 0) {
            openAnim -= 4;
        }

        if (this.inUp < 15) {
            this.inUp += 1.875;
        }


        GUIUtils.drawRectangle(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getWidth(), this.inUp, Gui.color2);

        if (openAnim < 100) {
            GlStateManager.pushMatrix();
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GUIUtils.glScissor(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getX() + this.parent.getWidth(), this.parent.getY() + this.offset + this.inUp, sr);
            GUIUtils.drawCircle(mouseX, mouseY, openAnim, Gui.color);
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
            GlStateManager.popMatrix();
        }

        if (openAnim == 100) {
            GUIUtils.drawRectangle(this.parent.getX(), this.parent.getY() + this.offset, this.parent.getWidth(), this.inUp, Gui.color);
        }

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        if (this.inUp >= 15) {
            GUIUtils.renderer.drawString(this.mod.getName(), (float) ((this.parent.getX() + 2) * 2) + 3.5f, (float) ((this.parent.getY() + this.offset + 2) * 2) - 1.5f, new Color(255, 255, 255, 255).getRGB(), true);
        }
        GL11.glPopMatrix();
        if (this.open && !this.subcomponents.isEmpty()) {
            int n = 0;
            for (Object componentw : this.subcomponents) {
                Component component = (Component) componentw;
                component.renderComponent();

                n += component.getHeight();

                if (!(component instanceof ModeMenu && component.opened)) {
                    GUIUtils.drawRectangle(this.parent.getX(), this.parent.getY() + this.offset + n + 15 - (component instanceof IntegerSlider || component instanceof DoubleSlider ? 10 : 0), component.getHoveredWidth(), (component.getHeight()), Gui.color);
                }
            }
        }
        if (open) {
            if (hovered2) {
                if (hoveredWidth < 3) {
                    hoveredWidth += 0.25;
                }
            } else if (hoveredWidth > 1) {
                hoveredWidth -= 0.25;
            }
        }

        if (!this.open && anim < 4) {
            anim += 0.5;
        } else if (this.open && anim > 0) {
            anim -= 0.5;
        }
        GUIUtils.drawRectangle((float) (this.parent.getX() + this.parent.getWidth() - 15), this.parent.getY() + this.offset + 6.5f - anim, 10.0f, 1.0f, new Color(255, 255, 255, 255).getRGB());
        GUIUtils.drawRectangle((float) (this.parent.getX() + this.parent.getWidth() - 15), this.parent.getY() + this.offset + 6.5f, 10.0f, 1.0f, new Color(255, 255, 255, 255).getRGB());
        GUIUtils.drawRectangle((float) (this.parent.getX() + this.parent.getWidth() - 15), this.parent.getY() + this.offset + 6.5f + anim, 10.0f, 1.0f, new Color(255, 255, 255, 255).getRGB());

        if (this.inUp >= 15 && open) {
            GUIUtils.drawRectangle(this.parent.getX(), this.parent.getY() + this.offset + 15, this.parent.getWidth(), this.inUp, Gui.color2);
            GL11.glPushMatrix();
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            GUIUtils.renderer.drawString("Keybind: " + (!this.pickingKey ? this.getKeyName(this.mod.getBind()) : ""), (float) ((this.parent.getX() + 2) * 2) + 3.5f, (float) ((this.parent.getY() + this.offset + 2) * 2) - 1.5f + 30, new Color(255, 255, 255, 255).getRGB(), true);
            GL11.glPopMatrix();
            if (pickingKey) {
                if (timer.passed(300)) {
                    GUIUtils.renderer.drawString("...", (float) this.parent.getX() + 50f, (float) this.parent.getY() + this.offset + 7, new Color(255, 255, 255, 255).getRGB(), true);
                    if (timer.passed(600)) {
                        timer.reset();
                    }
                }
            }
            GUIUtils.drawRectangle(this.parent.getX(), this.parent.getY() + this.offset + 15, this.hoveredWidth, 15, Gui.color);
        }
    }

    private String getKeyName(int key) {
        if (key != 0) {
            return Keyboard.getKeyName(key);
        }
        return "NONE";
    }

    @Override
    public void keyTyped(char c, int n) {
        for (Object componentw : this.subcomponents) {
            Component component = (Component) componentw;
            component.keyTyped(c, n);
        }
        if (open) {
            if (pickingKey && n != 14 && n != 211) {
                mod.setBind(n);
                pickingKey = false;
            } else if (pickingKey) {
                mod.setBind(0);
                pickingKey = false;
            }
        }
    }

    @Override
    public void updateComponent(int n, int n2) {
        if (!this.parent.isOpen()) {
            openAnim = 0;
        }
        this.isHovered = this.isMouseOnButton(n, n2);
        this.hovered2 = this.isMouseOnKeybind(n, n2);
        if (!this.subcomponents.isEmpty()) {
            for (Object componentw : this.subcomponents) {
                Component component = (Component) componentw;
                component.updateComponent(n, n2);
            }
        }

    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        for (Object componentw : this.subcomponents) {
            Component component = (Component) componentw;
            component.mouseReleased(n, n2, n3);
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        mouseX = n;
        mouseY = n2;

        if (this.isMouseOnButton(n, n2) && n3 == 0) {
            if (!ModuleManager.getModuleName(this.mod.name).isEnabled()) {
                ModuleManager.getModuleName(this.mod.name).enable();
            } else if (ModuleManager.getModuleName(this.mod.name).isEnabled()) {
                ModuleManager.getModuleName(this.mod.name).disable();
            }
            mc.world.playSound(mc.player, mc.player.getPosition(), SoundEvents.UI_BUTTON_CLICK, SoundCategory.PLAYERS, 1, 1);
        }
        if (this.isMouseOnKeybind(n, n2) && n3 == 0) {
            pickingKey = !pickingKey;
        }
        if (this.isMouseOnButton(n, n2) && n3 == 1) {
            this.open = !this.open;
            this.parent.refresh();
        }
        for (Object componentw : this.subcomponents) {
            Component component = (Component) componentw;
            component.mouseClicked(n, n2, n3);
        }
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > this.parent.getX() && n < this.parent.getX() + this.parent.getWidth() && n2 > this.parent.getY() + this.offset && n2 < this.parent.getY() + 15 + this.offset;
    }

    public boolean isMouseOnKeybind(int n, int n2) {
        return n > this.parent.getX() && n < this.parent.getX() + this.parent.getWidth() && n2 > this.parent.getY() + this.offset + 15 && n2 < this.parent.getY() + 30 + this.offset;
    }

    @Override
    public int getHeight() {
        if (this.open) {

            int n = 31;
            for (Object obj : subcomponents) {
                if (obj instanceof IntegerSlider || obj instanceof DoubleSlider) {
                    n += 25;
                } else if (obj instanceof ModeMenu) {
                    ModeMenu modeMenu = (ModeMenu) obj;
                    n += modeMenu.getHeight();
                } else if (obj instanceof ColorPicker) {
                    ColorPicker colorPicker = (ColorPicker) obj;
                    n += colorPicker.getHeight();
                } else {
                    n += 15;
                }
            }

            return n;

        }
        return 16;
    }
}