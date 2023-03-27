package com.thnkscj.toolkit.gui.click.components;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.click.Gui;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.modules.ModuleManager;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

public class Frame {

    private int x;
    private int y;
    public int dragX;
    public int dragY;
    private final int barHeight;
    private final int width;
    private boolean open;
    public boolean isDragging;
    private int offset;
    public Category category;
    public ArrayList components = new ArrayList();
    public double cntComp = 0.0;
    private Minecraft mnc;
    private double animFactor = 0;


    public Frame(Category category) {
        this.category = category;
        this.width = 100;
        this.x = 5;
        this.y = 50;
        this.barHeight = 16;
        this.dragX = 0;
        this.open = false;
        this.isDragging = false;
        int n = this.barHeight - 1;
        for (Object obj : ModuleManager.getModuleByCategory(category)) {
            Module module = (Module) obj;
            com.thnkscj.toolkit.gui.click.components.Button button = new com.thnkscj.toolkit.gui.click.components.Button(module, this, n);
            this.components.add(button);
            n += 16;
        }
        offset = n - 17;
    }


    public int getWidth() {
        return this.width;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean bl) {
        this.open = bl;
    }

    public void updatePosition(int n, int n2) {
        if (this.isDragging) {
            this.setX(n - this.dragX);
            this.setY(n2 - this.dragY);
        }
    }


    public void refresh() {
        int n = this.barHeight;
        for (Object components : this.components) {
            Component component = (Component) components;
            component.setOff(n);
            n += component.getHeight();
        }
        int n2 = this.barHeight - 1;
        for (Object obj : this.components) {
            com.thnkscj.toolkit.gui.click.components.Button component = (Button) obj;
            n2 += component.getHeight();
        }
        offset = n2 - 17;

    }

    public void renderFrame(int mouseX, int mouseY) {
        Gui.drawRect(this.x, this.y, this.width + x, this.barHeight + y, Gui.color);

        GL11.glPushMatrix();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        GUIUtils.renderer.drawString(this.category.name() + " [" + ModuleManager.getModuleByCategory(category).size() + "]", (float) (this.x * 2 + 5), ((float) this.y + 2.5f) * 2.0f + 1.0f, new Color(255, 255, 255, 255).getRGB(), true);
        GL11.glPopMatrix();

        GUIUtils.drawImage(new ResourceLocation("icons/" + category.name() + ".png"), x + 84, y + 2, 12, 12);

        if (this.open && !this.components.isEmpty()) {
            if (this.components.size() > (int) this.cntComp) {
                int n = 0;
                while ((double) n < this.cntComp) {
                    ((Component) this.components.get(n)).renderComponent();
                    ++n;
                }
                this.cntComp += 0.14;
            } else {
                for (int i = 0; i < this.components.size(); ++i) {
                    ((Component) this.components.get(i)).renderComponent();

                }
            }
        }
        if (this.open) {
            if (animFactor < 1) {
                animFactor += 0.1;
            } else if (animFactor > 1) {
                animFactor = 1;
            }

            GUIUtils.drawRectangle(x, y + this.barHeight, 1, (float) (this.offset * animFactor), Gui.color);
            GUIUtils.drawRectangle(x + this.width - 1, y + this.barHeight, 1, (float) (this.offset * animFactor), Gui.color);
            GUIUtils.drawRectangle(x, (float) (y + this.offset * animFactor + 16), this.width, 1, Gui.color);
        } else {
            if (animFactor > 0) {
                animFactor -= 0.1;
            }
        }
    }

    public boolean isWithinFrame(int x, int y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.barHeight;
    }

    public void setDrag(boolean bl) {
        this.isDragging = bl;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int n) {
        this.x = n;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int n) {
        this.y = n;
    }

    public ArrayList getComponents() {
        return this.components;
    }

    public static void getModCount() {
        int size = ModuleManager.getModules().size();

        for (int i = 0; i < size; ++i) {
            final Module m = ModuleManager.getModules().get(i);

            if (m != null) {

            }
        }
    }
}
