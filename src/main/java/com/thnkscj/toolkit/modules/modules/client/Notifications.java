package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.BooleanSetting;

public class Notifications extends Module {
    public static BooleanSetting chat = new BooleanSetting("Chat", "Chat", true);
    public static BooleanSetting render = new BooleanSetting("Render", "Render", true);

    public Notifications() {
        super("Notifications", "Notifications for chat or render", Category.CLIENT);

        addSettings(chat, render);
    }
}
