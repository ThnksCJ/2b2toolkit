package com.thnkscj.toolkit.proxy.ui;

import com.google.common.collect.Lists;
import com.thnkscj.toolkit.proxy.ProxyList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SideOnly(Side.CLIENT)
public class ProxySelectionList extends GuiListExtended {
    private final ProxyListGui owner;
    private final List<ProxyListEntry> serverListInternet = Lists.newArrayList();
    private int selectedSlotIndex = -1;

    public ProxySelectionList(ProxyListGui ownerIn, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn) {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.owner = ownerIn;
    }

    @NotNull
    public GuiListExtended.IGuiListEntry getListEntry(int index) {
        return this.serverListInternet.get(index);
    }

    protected int getSize() {
        return this.serverListInternet.size();
    }

    public void setSelectedSlotIndex(int selectedSlotIndexIn) {
        this.selectedSlotIndex = selectedSlotIndexIn;
    }

    protected boolean isSelected(int slotIndex) {
        return slotIndex == this.selectedSlotIndex;
    }

    public int getSelected() {
        return this.selectedSlotIndex;
    }

    public void updateOnlineServers(ProxyList proxyList) {
        this.serverListInternet.clear();

        for (int i = 0; i < proxyList.countServers(); ++i) {
            this.serverListInternet.add(new ProxyListEntry(this.owner, proxyList.getServerData(i)));
        }
    }

    protected int getScrollBarX() {
        return super.getScrollBarX() + 30;
    }

    public int getListWidth() {
        return super.getListWidth() + 85;
    }
}
