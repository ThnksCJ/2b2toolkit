package com.thnkscj.toolkit.gui.click.components.settings;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.Gui;
import com.thnkscj.toolkit.gui.click.components.Button;
import com.thnkscj.toolkit.setting.settings.ColorSetting;
import com.thnkscj.toolkit.util.misc.ColorUtil;
import com.thnkscj.toolkit.util.render.RenderUtil;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

public class ColorPicker extends com.thnkscj.toolkit.gui.click.components.Component {

    private final ColorSetting colorSetting;
    private final Button parent;
    int x;
    int y;
    int offset;
    private Color finalColor;
    private Color oldColor;
    private boolean pickeingColor;
    private float triangleSize = 0;
    boolean pickingColor = false;
    boolean pickingHue = false;
    boolean pickingAlpha = false;
    boolean hovered;
    int mx;
    int my;
    int radius = 0;
    int hueY;
    private boolean pipette = false;
    private float hoveredWidth = 1;
    private boolean hovered2;

    public ColorPicker(ColorSetting setting, Button button, int n) {
        this.colorSetting = setting;
        this.parent = button;
        this.x = button.parent.getX() + button.parent.getWidth();
        this.y = button.parent.getY() + button.offset;
        this.offset = n;
        this.hueY = parent.parent.getY() + offset + 20;
        this.finalColor = setting.getColor();
    }

    public boolean isMouseOnButton2(int n, int n2) {
        return n > x && n < x + parent.parent.getWidth() && n2 > y && n2 < y + 15;
    }

    @Override
    public void updateComponent(int mouseX, int mouseY) {
        this.hovered2 = this.isMouseOnButton2(mouseX, mouseY);
        this.hovered = this.isMouseOnButton(mouseX, mouseY);
        this.y = this.parent.parent.getY() + this.offset;
        this.x = this.parent.parent.getX();
        mx = mouseX;
        my = mouseY;
        colorSetting.setValue(finalColor);

        if (hovered2) {
            if (hoveredWidth < 3) {
                hoveredWidth += 0.25;
            }
        } else if (hoveredWidth > 1) {
            hoveredWidth -= 0.25;
        }
    }

    @Override
    public void mouseClicked(int n, int n2, int n3) {
        if (this.isMouseOnButton(n, n2) && n3 == 1 && this.parent.open) {
            if (this.opened) {
                this.opened = false;
                this.pipette = false;
            } else {
                this.opened = true;
            }
            parent.parent.refresh();

        }
        if (opened) {
            GUIUtils.drawRectangle(this.parent.parent.getX() + 87, this.parent.parent.getY() + offset + 95, 10, 10, new Color(255, 255, 255).getRGB());
            if (pipette) {
                if (!mouseOver(this.parent.parent.getX() + 87, this.parent.parent.getY() + offset + 95, this.parent.parent.getX() + 97, this.parent.parent.getY() + offset + 103, n, n2)) {
                    finalColor = new Color(ColorUtil.getPixelColor());
                    colorSetting.setColor(finalColor);
                } else {
                    pipette = false;
                }

            } else if (mouseOver(this.parent.parent.getX() + 87, this.parent.parent.getY() + offset + 95, this.parent.parent.getX() + 97, this.parent.parent.getY() + offset + 103, n, n2)) {
                pipette = true;

            }
            if (mouseOver(this.parent.parent.getX() + 74, this.parent.parent.getY() + offset + 95, this.parent.parent.getX() + 84, this.parent.parent.getY() + offset + 103, n, n2)) {
                colorSetting.rainbow = !colorSetting.rainbow;
            }
        }

        if (this.pickeingColor) {
            oldColor = finalColor;
        }
    }

    @Override
    public void mouseReleased(int n, int n2, int n3) {
        oldColor = finalColor;
        pickeingColor = false;
    }

    public boolean isMouseOnButton(int n, int n2) {
        return n > this.x && n < this.x + 108 && n2 > this.y && n2 < this.y + 16;
    }

    @Override
    public int getHeight() {
        if (this.opened) {
            return 110;
        } else {
            return 15;
        }
    }

    @Override
    public void setOff(int n) {
        this.offset = n;
    }


    public void drawPicker(ColorSetting setting, int mouseX, int mouseY) {
        float[] color = new float[]{
                0, 0, 0, 0
        };

        try {
            color = new float[]{
                    Color.RGBtoHSB(setting.getColor().getRed(), setting.getColor().getGreen(), setting.getColor().getBlue(), null)[0], Color.RGBtoHSB(setting.getColor().getRed(), setting.getColor().getGreen(), setting.getColor().getBlue(), null)[1], Color.RGBtoHSB(setting.getColor().getRed(), setting.getColor().getGreen(), setting.getColor().getBlue(), null)[2], setting.getColor().getAlpha() / 255f
            };
        } catch (Exception ignored) {

        }

        int pickerX = this.parent.parent.getX() + 4;
        int pickerY = this.parent.parent.getY() + this.offset + 16;
        int pickerWidth = 80;
        int pickerHeight = 75;

        int hueSliderX = this.parent.parent.getX() + 90;
        int hueSliderY = this.parent.parent.getY() + this.offset + 16;
        int hueSliderWidth = 5;
        int hueSliderHeight = 75;

        int alphaSliderX = this.parent.parent.getX() + 4;
        int alphaSliderY = this.parent.parent.getY() + this.offset + 95;
        int alphaSliderWidth = 68;
        int alphaSliderHeight = 10;

        pickeingColor = mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY);

        if (!pickingColor && !pickingHue && !pickingAlpha) {
            if (Mouse.isButtonDown(0) && mouseOver(pickerX, pickerY, pickerX + pickerWidth, pickerY + pickerHeight, mouseX, mouseY)) {
                pickingColor = true;

            } else if (Mouse.isButtonDown(0) && mouseOver(hueSliderX - 2, hueSliderY, hueSliderX + hueSliderWidth + 2, hueSliderY + hueSliderHeight, mouseX, mouseY)) {
                pickingHue = true;
            } else if (Mouse.isButtonDown(0) && mouseOver(alphaSliderX, alphaSliderY, alphaSliderX + alphaSliderWidth, alphaSliderY + alphaSliderHeight, mouseX, mouseY))
                pickingAlpha = true;
        }

        if (pickingHue) {
            if (!Mouse.isButtonDown(0)) {
                pickingHue = false;
            }
        }

        if (pickingColor) {
            if (!Mouse.isButtonDown(0)) {
                pickingColor = false;
            }
        }

        if (pickingAlpha) {
            if (!Mouse.isButtonDown(0)) {
                pickingAlpha = false;
            }
        }


        if (pickingHue) {
            float restrictedY = (float) Math.min(Math.max(hueSliderY, mouseY), hueSliderY + hueSliderHeight);
            color[0] = (restrictedY - (float) hueSliderY) / hueSliderHeight;
            color[0] = (float) Math.min(0.97, color[0]);
            oldColor = finalColor;
        }

        if (pickingAlpha) {
            float restrictedX = (float) Math.min(Math.max(alphaSliderX, mouseX), alphaSliderX + alphaSliderWidth);
            color[3] = 1 - (restrictedX - (float) alphaSliderX) / alphaSliderWidth;
        }

        if (pickingColor) {
            float restrictedX = (float) Math.min(Math.max(pickerX, mouseX), pickerX + pickerWidth);
            float restrictedY = (float) Math.min(Math.max(pickerY, mouseY), pickerY + pickerHeight);
            color[1] = (restrictedX - (float) pickerX) / pickerWidth;
            color[2] = 1 - (restrictedY - (float) pickerY) / pickerHeight;
            color[2] = (float) Math.max(0.04000002, color[2]);
            color[1] = (float) Math.max(0.022222223, color[1]);
        }

        int selectedColor = Color.HSBtoRGB(color[0], 1.0f, 1.0f);

        float selectedRed = (selectedColor >> 16 & 0xFF) / 255.0f;
        float selectedGreen = (selectedColor >> 8 & 0xFF) / 255.0f;
        float selectedBlue = (selectedColor & 0xFF) / 255.0f;

        drawPickerBase(pickerX, pickerY, pickerWidth, pickerHeight, selectedRed, selectedGreen, selectedBlue, 255);

        drawHueSlider(hueSliderX, hueSliderY, hueSliderWidth, hueSliderHeight, color[0]);

        int cursorX = (int) (pickerX + color[1] * pickerWidth);
        int cursorY = (int) ((pickerY + pickerHeight) - color[2] * pickerHeight);

        if (pickingColor) {
            if (radius < 9) {
                radius = radius + 1;
            }
        } else if (radius > 0) {
            radius = radius - 1;
        }

        finalColor = getColor(new Color(Color.HSBtoRGB(color[0], color[1], color[2])), color[3]);
        if (colorSetting.rainbow) {
            float[] hsb = Color.RGBtoHSB(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), null);
            float[] rainbow = Color.RGBtoHSB(ColorUtil.getRainbow().getRed(), ColorUtil.getRainbow().getGreen(), ColorUtil.getRainbow().getBlue(), null);
            int oldAlpha = finalColor.getAlpha();
            finalColor = new Color(Color.HSBtoRGB(rainbow[0], hsb[1], hsb[2]));
            finalColor = new Color(finalColor.getRed(), finalColor.getGreen(), finalColor.getBlue(), oldAlpha);
            colorSetting.setColor(finalColor);
        }

        GUIUtils.bottomCircle(cursorX, cursorY, radius, ColorUtil.changeAlpha(finalColor.getRGB(), 180));
        GUIUtils.topCircle(cursorX, cursorY, radius, ColorUtil.changeAlpha(oldColor.getRGB(), 180));
        GUIUtils.drawUnfilledCircle(cursorX, cursorY, 4, new Color(255, 255, 255).getRGB(), 1);

        drawAlphaSlider(alphaSliderX, alphaSliderY, alphaSliderWidth, alphaSliderHeight, finalColor.getRed() / 255f, finalColor.getGreen() / 255f, finalColor.getBlue() / 255f, color[3]);

        GUIUtils.drawRectangle(this.parent.parent.getX() + 87, this.parent.parent.getY() + offset + 95, 10, 10, new Color(255, 255, 255).getRGB());

        GUIUtils.drawRectangle(this.parent.parent.getX() + 88, this.parent.parent.getY() + offset + 96, 8, 8, new Color(220, 220, 220).getRGB());

        GUIUtils.drawRectangle(this.parent.parent.getX() + 74, this.parent.parent.getY() + offset + 95, 10, 10, new Color(255, 255, 255).getRGB());

        GUIUtils.drawRectangle(this.parent.parent.getX() + 75, this.parent.parent.getY() + offset + 96, 8, 8, ColorUtil.getRainbow().getRGB());

        if (colorSetting.rainbow) {
            GUIUtils.drawImage(new ResourceLocation("icons/check.png"), this.parent.parent.getX() + 75, this.parent.parent.getY() + offset + 96, 8, 8);
        }
    }


    public static boolean mouseOver(int minX, int minY, int maxX, int maxY, int mX, int mY) {
        return mX >= minX && mY >= minY && mX <= maxX && mY <= maxY;
    }


    public static Color getColor(Color color, float alpha) {
        final float red = (float) color.getRed() / 255;
        final float green = (float) color.getGreen() / 255;
        final float blue = (float) color.getBlue() / 255;
        return new Color(red, green, blue, alpha);
    }

    public static void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha) {
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glShadeModel(GL_SMOOTH);
        glBegin(GL_POLYGON);
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        glVertex2f(pickerX, pickerY);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glColor4f(red, green, blue, alpha);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glDisable(GL_ALPHA_TEST);
        glBegin(GL_POLYGON);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX, pickerY);
        glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
        glVertex2f(pickerX, pickerY + pickerHeight);
        glVertex2f(pickerX + pickerWidth, pickerY + pickerHeight);
        glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        glVertex2f(pickerX + pickerWidth, pickerY);
        glEnd();
        glEnable(GL_ALPHA_TEST);
        glShadeModel(GL_FLAT);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    public void drawHueSlider(int x, int y, int width, int height, float hue) {
        int step = 0;
        RenderUtil.drawRect(x, y, x + width, y + 4, 0xFFFF0000);

        for (int colorIndex = 0; colorIndex < 6; colorIndex++) {
            int previousStep = Color.HSBtoRGB((float) step / 6, 1.0f, 1.0f);
            int nextStep = Color.HSBtoRGB((float) (step + 1) / 6, 1.0f, 1.0f);
            RenderUtil.drawGradientRect(x, y + step * (height / 6f), x + width, y + (step + 1) * (height / 6f), previousStep, nextStep);
            step++;
        }
        int sliderMinY = (int) (y + height * hue);
        if (hueY > y + height) {
            hueY = y + height;
        } else if (hueY < y) {
            hueY = y;
        }
        if (parent.parent.isDragging) {
            hueY = sliderMinY;
        } else {
            if (sliderMinY > hueY) {
                hueY += 1;
            } else if (sliderMinY < hueY) {
                hueY -= 1;
            }
        }

        RenderUtil.drawTriangle(x - 3, hueY, 2, 90, new Color(255, 255, 255).getRGB());

        RenderUtil.drawTriangle(x + width + 3, hueY, 2, 270, new Color(255, 255, 255).getRGB());
    }


    public void drawAlphaSlider(int x, int y, int width, int height, float red, float green, float blue,
                                float alpha) {

        RenderUtil.drawLeftGradientRect(x, y, x + width, y + height, new Color(red, green, blue, 1).getRGB(), new Color(255, 255, 255).getRGB());
        int sliderMinX = (int) (x + width - (width * alpha));
        RenderUtil.drawRect(sliderMinX - 1, y, sliderMinX + 1, y + height, new Color(0).getRGB());

    }

    @Override
    public void renderComponent() {
        GUIUtils.drawRectangle(parent.parent.getX(), parent.parent.getY() + offset, parent.parent.getWidth(), this.getHeight(), Gui.color2);

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GUIUtils.renderer.drawString(colorSetting.getName(), parent.parent.getX() * 2 + 10, (parent.parent.getY() + offset + 2) * 2 - 2, new Color(255, 255, 255).getRGB(), true);
        GL11.glPopMatrix();

        GUIUtils.drawCircle(parent.parent.getX() + 85, parent.parent.getY() + offset + 7, 4, ColorUtil.changeAlpha(finalColor.getRGB(), 255));
        GUIUtils.drawCircle(parent.parent.getX() + 93, parent.parent.getY() + offset + 7, 4, ColorUtil.changeAlpha(finalColor.getRGB(), 255));
        GUIUtils.drawRectangle(parent.parent.getX() + 85, parent.parent.getY() + offset + 3, 8, 8, ColorUtil.changeAlpha(finalColor.getRGB(), 255));


        if (opened) {
            drawPicker(colorSetting, mx, my);
            if (pipette) {
                GUIUtils.drawImage(new ResourceLocation("icons/picker.png"), mx, my - 5, 10, 10);
            } else {
                GUIUtils.drawImage(new ResourceLocation("icons/picker.png"), this.parent.parent.getX() + 88, this.parent.parent.getY() + offset + 96, 8, 8);
            }
        } else {
            triangleSize = 0;
            this.hueY = parent.parent.getY() + offset + 18;
        }
        if (colorSetting.rainbow) {
            finalColor = colorSetting.getColor();
        }


    }

    @Override
    public float getHoveredWidth() {
        // temporary fix
        return 0;
    }
}