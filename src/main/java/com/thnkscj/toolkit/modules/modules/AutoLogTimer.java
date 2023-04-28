package com.thnkscj.toolkit.modules.modules;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import com.thnkscj.toolkit.util.misc.Timer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;
import java.util.Objects;

public class AutoLogTimer extends Module {
    public static final IntegerSetting threshold = new IntegerSetting("Threshold", "", 1, 10, 15);
    private boolean didQuit = false;
    private ServerData serverData;

    public AutoLogTimer() {
        super("AutoLogTimer", "Automatically logs you out after a set amount of time", Category.CLIENT);

        addSettings(threshold);
    }

    @Override
    public void onDisable() {
        didQuit = false;
        serverData = null;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        if (!didQuit) {
            if (mc.player == null || mc.world == null) {
                toggle();
                return;
            }
            if (mc.isSingleplayer()) {
                Command.sendMessage("Bro you are in single player lmao");
                toggle();
                return;
            }
            serverData = mc.getCurrentServerData();
            mc.world.sendQuittingDisconnectingPacket();
            didQuit = true;
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiDisconnected) {
            event.setGui(new AutoLogGui((GuiDisconnected) event.getGui(), serverData, threshold.getValue()));
        }
    }

    private class AutoLogGui extends GuiScreen {
        private final String reason;
        private final ITextComponent message;
        private final GuiScreen parentScreen;
        private final ServerData lastServer;
        private final Timer timer;
        private final int delay;
        private List<String> multilineMessage;
        private int textHeight;

        public AutoLogGui(final GuiDisconnected disconnected, final ServerData lastServer, final int delay) {
            this.parentScreen = disconnected.parentScreen;
            this.reason = disconnected.reason;
            this.message = disconnected.message;
            this.lastServer = lastServer;
            this.timer = new Timer();
            this.delay = delay;
        }

        @Override
        protected void keyTyped(final char typedChar, final int keyCode) {
        }

        @Override
        public void initGui() {
            this.buttonList.clear();
            this.multilineMessage = this.fontRenderer.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
            this.textHeight = this.multilineMessage.size() * this.fontRenderer.FONT_HEIGHT;
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRenderer.FONT_HEIGHT, this.height - 30), I18n.format("gui.toMenu")));
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, (int) Math.min(this.height / 1.85 + this.textHeight / 1.85 + this.fontRenderer.FONT_HEIGHT, this.height + 80), "Reconnect"));
        }

        @Override
        protected void actionPerformed(final GuiButton button) {
            switch (button.id) {
                case 0:
                    this.mc.displayGuiScreen(this.parentScreen);
                    break;
                case 1:
                    connectToLastServer();
                    break;
            }
        }

        @Override
        public void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
            this.drawDefaultBackground();
            this.drawCenteredString(this.fontRenderer, this.reason, this.width / 2, this.height / 2 - this.textHeight / 2 - this.fontRenderer.FONT_HEIGHT * 2, 11184810);
            int i = this.height / 2 - this.textHeight / 2;

            if (this.multilineMessage != null) {
                for (String s : this.multilineMessage) {
                    this.drawCenteredString(this.fontRenderer, s, this.width / 2, i, 16777215);
                    i += this.fontRenderer.FONT_HEIGHT;
                }
            }

            if (timer.passed(delay * 1000L)) {
                connectToLastServer();
            }

            float secondsLeft = delay - ((System.currentTimeMillis() - timer.getTime()) / 1000F);
            this.buttonList.get(1).displayString = "Reconnecting: " + Math.round(secondsLeft);

            super.drawScreen(mouseX, mouseY, partialTicks);
        }

        private void connectToLastServer() {
            didQuit = false;
            toggle();
            this.mc.displayGuiScreen(new GuiConnecting(this.parentScreen, this.mc, lastServer == null ? Objects.requireNonNull(mc.getCurrentServerData()) : lastServer));
        }
    }
}
