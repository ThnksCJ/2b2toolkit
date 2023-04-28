package com.thnkscj.toolkit.modules.modules;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.Gui;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.BooleanSetting;
import com.thnkscj.toolkit.setting.settings.ColorSetting;
import com.thnkscj.toolkit.setting.settings.EnumSetting;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGui extends Module {
    public static BooleanSetting catEars = new BooleanSetting("CatEars", "", true);
    public static IntegerSetting animationSpeed = new IntegerSetting("Animation Speed", "", 1, 3, 10);
    public static ColorSetting color = new ColorSetting("Foreground", "", new Color(151, 24, 255), false);
    public static ColorSetting color2 = new ColorSetting("Background", "", new Color(0, 0, 0, 100), false);
    public static EnumSetting<fontEnum> font = new EnumSetting<>("Font", "", fontEnum.Greycliff);
    public static EnumSetting<BackgroundEnum> background = new EnumSetting<>("BackgroundMode", "", BackgroundEnum.Gradient);

    public ClickGui() {
        super("ClickGui", "", Category.CLIENT);
        setBind(Keyboard.KEY_RSHIFT);

        addSettings(catEars, color, color2, font, background, animationSpeed);
    }

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

    public enum fontEnum {Blazma, Comfortaa, Greycliff, Tektur}

    public enum BackgroundEnum {None, Static, Blur, Both, Gradient, RainbowGradient, Dark, Blark}
}