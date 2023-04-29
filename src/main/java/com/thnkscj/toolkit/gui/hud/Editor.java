package com.thnkscj.toolkit.gui.hud;

import com.thnkscj.toolkit.gui.click.components.Frame;
import com.thnkscj.toolkit.modules.Category;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;

public class Editor extends GuiScreen {
    static Editor INSTANCE = new Editor();
    public static ArrayList<Frame> frames;

    public Editor() {
        setInstance();

        frames = new ArrayList<>();

        Frame frame = new Frame(Category.CLIENT);
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

    public void drawScreen(int x, int y, float partialTicks) {

    }
}
