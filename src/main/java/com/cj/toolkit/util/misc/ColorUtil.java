package com.cj.toolkit.util.misc;

import com.cj.toolkit.event.EventManger;

import java.awt.*;


public class ColorUtil {

    public static int getPixelColor() {
        int c = 0;
        try {
            Point coord = null;
            Robot robot = new Robot();
            coord = MouseInfo.getPointerInfo().getLocation();
            Color color = robot.getPixelColor((int) coord.getX(), (int) coord.getY());
            c = color.getRGB();

        } catch (AWTException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static int getInt() {
        return EventManger.INSTANCE.getRgb();
    }

    public static Color getColor() {
        return EventManger.INSTANCE.getColour();
    }

    public static Color getRainbow() {
        return Color.getHSBColor((float) (System.currentTimeMillis() % 7500L) / 7500f, 0.85f, 0.85f);
    }

    public static int changeAlpha(int origColor, int alphaValue) {

        origColor = origColor & 0x00FFFFFF;
        return (alphaValue << 24) | origColor;
    }

    public static int colorToDecimal(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();

        String hex = String.format("%02X%02X%02X", r, g, b);
        int num = Integer.parseInt(hex, 16);
        System.out.println("hex: " + hex);
        System.out.println("dec: " + num);

        return num;
    }

    public static String colorToHex(Color color) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int a = color.getAlpha();

        return String.format("%02X%02X%02X%02X", a, r, g, b);
    }

    public static int getDecimal(int red, int green, int blue) {
        return red + 256 * green + 256 * 256 * blue;
    }

    private static String toHex(int i) {
        String result;

        result = Integer.toHexString(i);
        if (result.length() % 2 == 1)
            result = "0" + result;

        return result;
    }

    public static String toHex(Color color) {
        return "#" + toHex(color.getAlpha()) + toHex(color.getRed()) + toHex(color.getGreen()) + toHex(color.getBlue());
    }

    public static Color hexToColor(String hex) {

        hex = hex.replace("#", "");

        if (hex.length() == 8) {
            int a = Integer.valueOf(hex.substring(0, 2), 16);
            int r = Integer.valueOf(hex.substring(2, 4), 16);
            int g = Integer.valueOf(hex.substring(4, 6), 16);
            int b = Integer.valueOf(hex.substring(6, 8), 16);
            return new Color(r, g, b, a);
        }

        return Color.BLACK;
    }
}
