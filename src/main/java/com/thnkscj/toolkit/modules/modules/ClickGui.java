package com.thnkscj.toolkit.modules.modules;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.Gui;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.ColorSetting;
import com.thnkscj.toolkit.setting.settings.EnumSetting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGui extends Module {
    public ClickGui() {
        super("ClickGui", "", Category.CLIENT);
        setBind(Keyboard.KEY_RSHIFT);

        addSettings(color, color2, font, background);
    }

    public enum fontEnum {Blazma, Comfortaa, Greycliff, Tektur}

    public enum formatEnum {Plain, Bold, Italic, BoldItalic}

    public enum BackgroundEnum {None, Static, Blur, Both, Gradient, RainbowGradient, Dark, Blark}

    public ColorSetting color = new ColorSetting("Foreground", "", new Color(151, 24, 255), false);
    public ColorSetting color2 = new ColorSetting("Background", "", new Color(0, 0, 0, 100), false);
    public EnumSetting<fontEnum> font = new EnumSetting<>("Font", "", fontEnum.Greycliff);
    public EnumSetting<formatEnum> format = new EnumSetting<>("Format", "", formatEnum.Bold);
    public EnumSetting<BackgroundEnum> background = new EnumSetting<>("BackgroundMode", "", BackgroundEnum.Gradient);

    @Override
    public void onEnable() {
        Thread t = new Thread();
        try {
            GUIUtils.updateFont();
            loadShader();
            mc.displayGuiScreen(Gui.getInstance());
        } catch (Exception ignored) {
        }

        t.start();
    }

    public void loadShader() {
        if (background.getValue() == BackgroundEnum.Blur || background.getValue() == BackgroundEnum.Both) {
            mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blark.json"));
        }
    }

    public void stopShader() {
        if (background.getValue() == BackgroundEnum.Blur || background.getValue() == BackgroundEnum.Both) {
            mc.entityRenderer.stopUseShader();
        }
    }

    public void updateShader() {
        try {
            if (!(background.getValue() == BackgroundEnum.Blur || background.getValue() == BackgroundEnum.Both)) {
                mc.entityRenderer.getShaderGroup().deleteShaderGroup();
            } else if (background.getValue() == BackgroundEnum.Blur || background.getValue() == BackgroundEnum.Both) {
                mc.entityRenderer.loadShader(new ResourceLocation("shaders/post/blark.json"));
            }
        } catch (Exception ignored) {
        }
    }
}