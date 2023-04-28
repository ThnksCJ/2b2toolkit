package com.thnkscj.toolkit.event;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.command.CommandManager;
import com.thnkscj.toolkit.event.events.network.PacketEvent;
import com.thnkscj.toolkit.event.events.render.Render2DEvent;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.modules.ModuleManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

import static com.thnkscj.toolkit.command.Command.mc;
import static org.lwjgl.opengl.GL11.*;

public class EventManger {

    public static EventManger INSTANCE;
    Color color;
    int rgb;
    CommandManager commandManager = new CommandManager();

    public EventManger() {
        MinecraftForge.EVENT_BUS.register(this);
        INSTANCE = this;

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

    @SubscribeEvent
    public void onChatSent(PacketEvent.Send event) {
        final Packet<?> packet = event.getPacket();

        if (!(packet instanceof CPacketChatMessage))
            return;

        final CPacketChatMessage cPacketChatMessage = (CPacketChatMessage) packet;

        final String message = cPacketChatMessage.getMessage();

        if (!message.startsWith(Command.getPrefix()))
            return;

        event.setCanceled(true);
        commandManager.callCommand(message.substring(1));
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
        if (event.getType().equals(RenderGameOverlayEvent.ElementType.TEXT)) {

            boolean blend = glIsEnabled(GL_BLEND);
            boolean depth = glIsEnabled(GL_DEPTH_TEST);

            ScaledResolution resolution = new ScaledResolution(mc);
            Render2DEvent render2DEvent = new Render2DEvent(event.getPartialTicks(), resolution);
            for (Module module : ModuleManager.getModules()) {
                if (!module.isEnabled()) continue;
                module.onRender2D(render2DEvent);
            }
        }
    }

    @SubscribeEvent
    public void onWorldRender(RenderWorldLastEvent event) {
        for (Module module : ModuleManager.getModules()) {
            if (!module.isEnabled()) continue;
            module.onWorldRender(event);
        }
    }

    @SubscribeEvent
    public void renderEntityModel(RenderWorldLastEvent event) {
        for (Module module : ModuleManager.getModules()) {
            if (!module.isEnabled()) continue;
            module.onWorldRender(event);
        }
    }
}