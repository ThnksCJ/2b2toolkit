package com.thnkscj.toolkit.gui.hud;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.config.SaveLoad;
import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.components.Component;
import com.thnkscj.toolkit.gui.click.components.Frame;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.HudModule;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.client.ClickGui;
import com.thnkscj.toolkit.modules.modules.client.HudEditor;
import com.thnkscj.toolkit.util.misc.ColorUtil;
import com.thnkscj.toolkit.util.shader.RoundedUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Editor extends GuiScreen {
    static Editor INSTANCE = new Editor();
    public static ArrayList<Frame> frames;
    public static int lastMouseX, lastMouseY;
    public static HudModule dragging;
    public static int color = ClickGui.color.getColor().getRGB();
    public static int color2 = ClickGui.color2.getColor().getRGB();

    public Editor() {
        setInstance();

        frames = new ArrayList<>();

        Frame frame = new Frame(Category.HUD);
        frame.setX(60);
        frame.setY(10);
        frames.add(frame);
    }

    public static Editor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Editor();
            MinecraftForge.EVENT_BUS.register(INSTANCE);
        }
        return INSTANCE;
    }

    private void setInstance() {
        INSTANCE = this;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        color = ClickGui.color.getColor().getRGB();
        color2 = ClickGui.color2.getColor().getRGB();

        if (ClickGui.background.getValue() == ClickGui.BackgroundEnum.RainbowGradient) {
            Color c2 = ColorUtil.getRainbow();
            float[] c3 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
            int color3 = Color.HSBtoRGB(c3[0] + 10, c3[1], c3[2]);

            GUIUtils.drawSidewaysGradientRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), ColorUtil.changeAlpha(c2.getRGB(), 150), ColorUtil.changeAlpha(color3, 150));
        } else {
            if (ClickGui.background.getValue() == ClickGui.BackgroundEnum.Static || ClickGui.background.getValue() == ClickGui.BackgroundEnum.Both) {
                GUIUtils.drawRectangle(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), ColorUtil.changeAlpha(color, 130));
            } else {
                if (ClickGui.background.getValue() == ClickGui.BackgroundEnum.Gradient) {
                    GUIUtils.drawGradientRect(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), ColorUtil.changeAlpha(color, 20), ColorUtil.changeAlpha(color, 230));
                } else {
                    if (ClickGui.background.getValue() == ClickGui.BackgroundEnum.Dark || ClickGui.background.getValue() == ClickGui.BackgroundEnum.Blark) {
                        GUIUtils.drawRectangle(0, 0, scaledResolution.getScaledWidth(), scaledResolution.getScaledHeight(), new Color(0, 0, 0, 130).getRGB());
                    }
                }
            }
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(1, 1, 1);
        for (Frame frame : frames) {
            frame.renderFrame(mouseX, mouseY);
            frame.updatePosition(mouseX, mouseY);
            for (Component component : frame.getComponents()) {
                component.updateComponent(mouseX, mouseY);
            }
        }
        this.updateMouseWheel();

        for (HudModule component : HudModule.components) {
            if (component.shouldRender()) {
                for (HudModule.HudPoint point : component.renderedPoints) {
                    RoundedUtil.drawRound(point.x - 1, point.y - 1, point.x2 - component.xPos + 2, point.y2 - component.yPos + 2, 1, true, new Color(0x80000000, true));

                    if (HudEditor.debug.isEnabled()) {
                        GUIUtils.drawCircle((int) point.x, (int) point.y, 1, 0xffff0000);
                        GUIUtils.drawCircle((int) point.x2, (int) point.y - 1, 1, 0xffff0000);
                        GUIUtils.drawCircle((int) point.x, (int) point.y2, 1, 0xffff0000);
                        GUIUtils.drawCircle((int) point.x2, (int) point.y2, 1, 0xffff0000);
                    }

                    if (component.isSelected || mouseX >= point.x - 3 && mouseX <= point.x2 - 1 && mouseY >= point.y - 3 && mouseY <= point.y2 + 2) {
                        int color = new Color(45, 207, 255, 255).getRGB();

                        GUIUtils.drawCircle((int) point.x - 2, (int) point.y - 2, 1, color);
                        GUIUtils.drawCircle((int) point.x2 + 2, (int) point.y - 2, 1, color);
                        GUIUtils.drawCircle((int) point.x - 2, (int) point.y2 + 1, 1, color);
                        GUIUtils.drawCircle((int) point.x2 + 2, (int) point.y2 + 1, 1, color);

                        GUIUtils.drawLine((int) point.x - 2, (int) point.y - 2, (int) point.x2 + 2, (int) point.y - 2, color);
                        GUIUtils.drawLine((int) point.x - 2, (int) point.y - 2, (int) point.x - 2, (int) point.y2 + 1, color);
                        GUIUtils.drawLine((int) point.x2 + 2, (int) point.y - 2, (int) point.x2 + 2, (int) point.y2 + 1, color);
                        GUIUtils.drawLine((int) point.x - 2, (int) point.y2 + 1, (int) point.x2 + 2, (int) point.y2 + 1, color);
                    }
                }

                component.onRender(partialTicks);
            }
        }

        if (dragging != null) {
            if (Mouse.isButtonDown(0)) {
                dragging.xPos += mouseX - lastMouseX;
                dragging.yPos += mouseY - lastMouseY;
            } else {
                dragging = null;
            }
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int x, int y, int button) {
        for (Frame frame : frames) {
            if (frame.isWithinFrame(x, y) && button == 0) {
                frame.setDrag(true);
                frame.dragX = x - frame.getX();
                frame.dragY = y - frame.getY();
            }
            if (frame.isWithinFrame(x, y) && button == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.mouseClicked(x, y, button);
            }
        }

        HudModule component = null;
        for (HudModule component2 : HudModule.components) {
            if (component2.shouldRender()) {
                for (HudModule.HudPoint point : component2.renderedPoints) {
                    if (point.x2 + 1 > lastMouseX && point.x - 1 < lastMouseX && point.y2 + 1 > lastMouseY && point.y - 1 < lastMouseY) {
                        component = component2;
                        component.isSelected = true;
                        break;
                    }
                }
            }
        }

        if (component == null) {
            for (HudModule component2 : HudModule.components) {
                if (component2 != component) {
                    component2.isSelected = false;
                }
            }
            return;
        }

        if (button == 0) {
            dragging = component;
        }
    }

    protected void mouseReleased(int x, int y, int button) {
        for (Frame frame : frames) {
            frame.setDrag(false);
        }
        for (Frame frame : frames) {
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.mouseReleased(x, y, button);
            }
        }
    }

    protected void keyTyped(char c, int n) {
        for (Frame frame : frames) {
            if (!frame.isOpen() || n == 1 || frame.getComponents().isEmpty()) continue;
            for (Component component : frame.getComponents()) {
                component.keyTyped(c, n);
            }
        }
        if (n == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    public void updateMouseWheel() {
        int scrollWheel = Mouse.getDWheel();
        for (Frame frame : frames) {
            if (scrollWheel < 0) {
                frame.setY(frame.getY() - 15);
                continue;
            }
            if (scrollWheel == 0) continue;
            frame.setY(frame.getY() + 15);
        }
    }

    public void onGuiClosed() {
        ModuleManager.getModule(HudEditor.class).disable();

        try {
            SaveLoad.saveModules();
        } catch (IOException ignored) {
            Command.sendErrMessage("Failed to save modules");
        }
    }
}
