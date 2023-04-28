package com.thnkscj.toolkit.proxy.ui;

import com.thnkscj.toolkit.proxy.ProxyList;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.network.ServerPinger;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ProxyListGui extends GuiScreen {
    public final ServerPinger oldServerPinger = new ServerPinger();
    private final GuiScreen parentScreen;
    private ProxySelectionList proxyListSelector;
    private boolean initialized;
    private GuiButton btnSelectServer;

    public ProxyListGui(GuiScreen parentScreen) {
        this.parentScreen = parentScreen;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        if (this.initialized) {
            this.proxyListSelector.setDimensions(this.width, this.height, 32, this.height - 64);
        } else {
            this.initialized = true;
            ProxyList savedServerList = new ProxyList();

            this.proxyListSelector = new ProxySelectionList(this, this.mc, this.width, this.height, 32, this.height - 64, 36);
            this.proxyListSelector.updateOnlineServers(savedServerList);
        }

        this.createButtons();
    }

    public void createButtons() {
        this.btnSelectServer = this.addButton(new GuiButton(0, this.width / 2 - 110, this.height - 52, 70, 20, "Join"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 35, this.height - 52, 70, 20, I18n.format("selectServer.refresh")));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 40, this.height - 52, 70, 20, I18n.format("gui.cancel")));
        this.buttonList.add(new GuiButton(3, this.width / 2 - 50, this.height - 28, 100, 20, "Join Proxy Hub"));
        this.selectServer(this.proxyListSelector.getSelected());
    }

    @Override
    public void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0:
                this.connectToSelected();
                break;
            case 1:
                this.refreshServerList();
                break;
            case 2:
                this.mc.displayGuiScreen(this.parentScreen);
                break;
            case 3:
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.proxyListSelector.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRenderer, "Proxy Selection Screen", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.proxyListSelector.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        this.proxyListSelector.mouseReleased(mouseX, mouseY, state);
    }

    public void selectServer(int index) {
        this.proxyListSelector.setSelectedSlotIndex(index);
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = index < 0 ? null : this.proxyListSelector.getListEntry(index);

        this.btnSelectServer.enabled = guilistextended$iguilistentry != null;
    }

    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        this.proxyListSelector.handleMouseInput();
    }

    public void updateScreen() {
        super.updateScreen();

        oldServerPinger.pingPendingNetworks();
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);

        oldServerPinger.clearPendingNetworks();
    }

    private void refreshServerList() {
        this.mc.displayGuiScreen(new ProxyListGui(this.parentScreen));
    }

    public void connectToSelected() {
        GuiListExtended.IGuiListEntry guilistextended$iguilistentry = this.proxyListSelector.getSelected() < 0 ? null : this.proxyListSelector.getListEntry(this.proxyListSelector.getSelected());

        if (guilistextended$iguilistentry instanceof ProxyListEntry) {
            this.mc.displayGuiScreen(new GuiConnecting(this.parentScreen, this.mc, ((ProxyListEntry) guilistextended$iguilistentry).getServerData()));
        }
    }
}

