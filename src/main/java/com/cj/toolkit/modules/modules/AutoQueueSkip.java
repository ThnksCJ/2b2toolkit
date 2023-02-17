package com.cj.toolkit.modules.modules;

import com.cj.toolkit.modules.Category;
import com.cj.toolkit.modules.Module;
import com.cj.toolkit.setting.settings.BooleanSetting;
import com.cj.toolkit.setting.settings.IntegerSetting;
import com.cj.toolkit.util.misc.TimerUtil;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.GuiOpenEvent;

import java.util.Objects;
import java.util.regex.Pattern;

public class AutoQueueSkip extends Module {
    public AutoQueueSkip() {
        super("AutoQueueSkip", "Automatically skips the queue", Category.CLIENT);

        addSettings(threshold);
    }

    public static final IntegerSetting threshold = new IntegerSetting("Threshold", "When to preform preform skip", 1, 6, 15);
    public static final BooleanSetting reconnect = new BooleanSetting("Reconnect", "Reconnect queue skip", true);

    private final Pattern pattern = Pattern.compile("Server restarting in " + threshold.getValue());
    private final TimerUtil timer = new TimerUtil();

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChat(ClientChatReceivedEvent event) {
        if (pattern.matcher(event.getMessage().getUnformattedText()).find()) {
            mc.world.sendQuittingDisconnectingPacket();
            mc.displayGuiScreen(new GuiConnecting(null, mc, Objects.requireNonNull(mc.getCurrentServerData())));
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        this.timer.reset();
        if (event.getGui() instanceof GuiDisconnected) {
            if (reconnect.getValue()) {
                while(this.timer.passed(threshold.getValue() * 1000L)) {
                    mc.displayGuiScreen(new GuiConnecting(null, mc, Objects.requireNonNull(mc.getCurrentServerData())));
                    this.timer.reset();
                }
            }
        }
    }
}
