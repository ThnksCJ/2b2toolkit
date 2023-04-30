package com.thnkscj.toolkit.gui.click;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.components.Component;
import com.thnkscj.toolkit.gui.click.components.Frame;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.client.ClickGui;
import com.thnkscj.toolkit.util.misc.ColorUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class Gui extends GuiScreen {
    public static ArrayList<Frame> frames;
    public static int color = ClickGui.color.getColor().getRGB();
    public static int color2 = ClickGui.color2.getColor().getRGB();
    static Gui INSTANCE = new Gui();
    private float scale = 0;

    public Gui() {
        setInstance();

        frames = new ArrayList<>();

        Frame frame = new Frame(Category.CLIENT);
        frame.setX(30);
        frame.setY(10);
        frames.add(frame);
    }

    public static Gui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Gui();
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

    protected void mouseClicked(int n, int n2, int n3) {
        for (Frame frame : frames) {
            if (frame.isWithinFrame(n, n2) && n3 == 0) {
                frame.setDrag(true);
                frame.dragX = n - frame.getX();
                frame.dragY = n2 - frame.getY();
            }
            if (frame.isWithinFrame(n, n2) && n3 == 1) {
                frame.setOpen(!frame.isOpen());
            }
            if (!frame.isOpen() || frame.getComponents().isEmpty()) continue;
            for (Object componentw : frame.getComponents()) {
                Component component = (Component) componentw;
                component.mouseClicked(n, n2, n3);
            }
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

    public void drawScreen(int x, int y, float partialTicks) {
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

        if (scale < 1) {
            scale += 0.1;
        }

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        for (Frame frame : frames) {
            frame.renderFrame(x, y);
            frame.updatePosition(x, y);
            for (Object componentw : frame.getComponents()) {
                Component component = (Component) componentw;
                component.updateComponent(x, y);
            }
        }
        GlStateManager.popMatrix();
        this.updateMouseWheel();
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
        ModuleManager.getModuleName("ClickGui").setEnabled(false);
        ModuleManager.getModule(ClickGui.class).stopShader();
        scale = 0;
    }

    public void initGui() {
        scale = 0;
    }
}