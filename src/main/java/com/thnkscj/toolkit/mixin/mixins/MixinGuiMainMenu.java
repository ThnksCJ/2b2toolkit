package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.proxy.ui.ProxyButton;
import com.thnkscj.toolkit.proxy.ui.ProxyListGui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({GuiMainMenu.class})
public abstract class MixinGuiMainMenu extends GuiScreen {
    @Inject(method = "initGui", at = @At("RETURN"))
    public void initGui(CallbackInfo info) {
        this.buttonList.add(new ProxyButton(10, 35));
        super.initGui();
    }

    @Inject(method = "actionPerformed", at = @At("HEAD"))
    public void actionPerformed(GuiButton button, CallbackInfo info) {
        if (button.id == 8068) {
            this.mc.displayGuiScreen(new ProxyListGui(this));
        }
    }
}
