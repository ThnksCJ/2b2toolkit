package com.cj.toolkit.setting.settings;

import com.cj.toolkit.setting.Setting;
import com.cj.toolkit.util.misc.ColorUtil;

import java.awt.*;

public final class ColorSetting extends Setting<Color> {

    public ColorSetting(final String name, final String description, final Color value, final boolean rbow) {
        super(name, description, value);

        this.rainbow = rbow;
    }


    public Color getColor() {
        if (this.rainbow) {
            float[] hsb = Color.RGBtoHSB(value.getRed(), value.getGreen(), value.getBlue(), null);
            float[] rainbow = Color.RGBtoHSB(ColorUtil.getRainbow().getRed(), ColorUtil.getRainbow().getGreen(), ColorUtil.getRainbow().getBlue(), null);
            int oldAlpha = value.getAlpha();
            Color finalColor = new Color(Color.HSBtoRGB(rainbow[0], hsb[1], hsb[2]));
            finalColor = new Color(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), oldAlpha);

            value = finalColor;
        }
        return value;
    }

    public void setColor(Color color) {

        this.value = color;
    }


    public void setRainbow(boolean val) {
        this.rainbow = val;
    }

    public boolean rainbow;
}