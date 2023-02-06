package com.cj.toolkit.event;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.command.CommandManager;
import com.cj.toolkit.modules.ModuleManager;
import com.cj.toolkit.modules.Module;
import com.cj.toolkit.Toolkit;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

import static com.cj.toolkit.command.Command.mc;

public class EventManger {

    Color color;
    int rgb;
    CommandManager commandManager = new CommandManager();

    public static EventManger INSTANCE;

    public EventManger() {
        MinecraftForge.EVENT_BUS.register(this);
        INSTANCE = this;

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled = true)
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.getEventKeyState()) {
            if (Keyboard.getEventKey() == 0) return;
            ModuleManager.onBind(Keyboard.getEventKey());

        }
    }

    public int getRgb() {
        return rgb;
    }

    public Color getColour() {
        return color;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (mc.player != null)
            ModuleManager.onUpdate();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onChatSent(ClientChatEvent event) {
        if(event.getMessage() == null || event.getMessage().isEmpty()) return;

        // causes a crash when you type a command
        /*
        if (event.getMessage().startsWith(Command.getPrefix())) {
            event.setCanceled(true);
            try {
                mc.ingameGUI.getChatGUI().addToSentMessages(event.getMessage());
                commandManager.callCommand(event.getMessage().substring(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
         */
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        for (Module module : ModuleManager.getModules()) {
            if (!module.isEnabled()) continue;
            module.onWorldRender();
        }
    }

    @SubscribeEvent
    public void renderEntityModel(RenderWorldLastEvent event) {
        for (Module module : ModuleManager.getModules()) {
            if (!module.isEnabled()) continue;
            module.onWorldRender();
        }
    }
}