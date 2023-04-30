package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.gui.GUIUtils;
import com.thnkscj.toolkit.gui.hud.Editor;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import org.lwjgl.input.Keyboard;

public class HudEditor extends Module {
    public HudEditor() {
        super("HudEditor", "", Category.CLIENT);
        setBind(Keyboard.KEY_RSHIFT);
    }

    @Override
    public void onEnable() {
        Thread t = new Thread();
        try {
            GUIUtils.updateFont();
            mc.displayGuiScreen(Editor.getInstance());
        } catch (Exception ignored) {
        }

        t.start();
    }
}
