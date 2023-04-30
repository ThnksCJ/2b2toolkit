package com.thnkscj.toolkit.gui.hud;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.components.Component;
import com.thnkscj.toolkit.gui.click.components.Frame;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.HudModule;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.client.ClickGui;
import com.thnkscj.toolkit.modules.modules.client.HudEditor;
import com.thnkscj.toolkit.util.misc.ColorUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class Editor extends GuiScreen {
    static Editor INSTANCE = new Editor();
    public static ArrayList<Frame> frames;
    public static int lastMouseX, lastMouseY;
    public static HudModule dragging;
    public static int EXTEND = 3;
    public static int color = ClickGui.color.getColor().getRGB();
    public static int color2 = ClickGui.color2.getColor().getRGB();
    private float scale = 0;

    public Editor() {
        setInstance();

        frames = new ArrayList<>();

        Frame frame = new Frame(Category.HUD);
        frame.setX(30);
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

    public void initGui() {
        scale = 1;
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
        GlStateManager.scale(scale, scale, 1);
        for (Frame frame : frames) {
            frame.renderFrame(mouseX, mouseY);
            frame.updatePosition(mouseX, mouseY);
            for (Object componentw : frame.getComponents()) {
                Component component = (Component) componentw;
                component.updateComponent(mouseX, mouseY);
            }
        }
        this.updateMouseWheel();

        double guiScale = mc.gameSettings.guiScale;
        GlStateManager.scale(guiScale, guiScale, guiScale);

        mouseX = (int) (mouseX / guiScale);
        mouseY = (int) (mouseY / guiScale);

        for (HudModule component : HudModule.components) {
            if (component.shouldRender()) {
                for (HudModule.HudPoint point : component.renderedPoints) {
                    drawRect((int) point.x - EXTEND, (int) point.y - EXTEND, (int) point.x2 + EXTEND, (int) point.y2 + EXTEND, 0x80000000);
                }
            }
        }

        for (HudModule component : HudModule.components) {
            if (component.shouldRender()) {
                if (!component.applyScaling) {
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                }

                component.onRender(partialTicks);

                if (!component.applyScaling) {
                    GlStateManager.popMatrix();
                    GlStateManager.pushMatrix();
                }
            }
        }

        if (dragging != null) {
            if (Mouse.isButtonDown(0)) {
                dragging.xAdd += mouseX - lastMouseX;
                dragging.yAdd += mouseY - lastMouseY;
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
            for (Object componentw : frame.getComponents()) {
                Component component = (Component) componentw;
                component.mouseClicked(x, y, button);
            }
        }

        HudModule component = null;
        for (HudModule component2 : HudModule.components) {
            if (component2.shouldRender()) {
                for (HudModule.HudPoint point : component2.renderedPoints) {
                    if (point.x2 + EXTEND > lastMouseX && point.x - EXTEND < lastMouseX && point.y2 + EXTEND > lastMouseY && point.y - EXTEND < lastMouseY) {
                        component = component2;
                        break;
                    }
                }
            }
        }

        if (component == null) {
            return;
        }

        if (button == 0) {
            dragging = component;
        } else if (button == 2) {
            component.xAdd = 0;
            component.yAdd = 0;
        }
    }

    protected void mouseReleased(int x, int y, int button) {
        for (Frame frame : frames) {
            frame.setDrag(false);
        }
        for (Frame frame : frames) {
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Object componentw : frame.getComponents()) {
                Component component = (Component) componentw;
                component.mouseReleased(x, y, button);
            }
        }
    }

    protected void keyTyped(char c, int n) {
        for (Frame frame : frames) {
            if (!frame.isOpen() || n == 1 || frame.getComponents().isEmpty()) continue;
            for (Object componentw : frame.getComponents()) {
                Component component = (Component) componentw;
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
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!(mc.currentScreen instanceof Editor)) {
            ModuleManager.getModule(HudEditor.class).disable();
        }
    }
}
