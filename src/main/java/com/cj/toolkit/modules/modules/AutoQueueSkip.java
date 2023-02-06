package com.cj.toolkit.modules.modules;

import com.cj.toolkit.modules.Category;
import com.cj.toolkit.modules.Module;
import com.cj.toolkit.setting.settings.IntegerSetting;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;
import java.util.regex.Pattern;

public class AutoQueueSkip extends Module {
    public AutoQueueSkip() {
        super("AutoQueueSkip", "Automatically skips the queue", Category.CLIENT);

        addSettings(threshold);
    }

    public static final IntegerSetting threshold = new IntegerSetting("Threshold", "When to disconnect from 2b2t", 1, 6, 15);

    Pattern pattern = Pattern.compile("Server restarting in " + threshold.getValue());

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        if (pattern.matcher(event.getMessage().getUnformattedText()).find()) {
            mc.displayGuiScreen(new GuiConnecting(null, mc, Objects.requireNonNull(mc.getCurrentServerData())));
        }
    }
}
