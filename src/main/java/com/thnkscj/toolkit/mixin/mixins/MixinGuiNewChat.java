package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.modules.modules.client.GameplayTweaks;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.thnkscj.toolkit.util.Wrapper.mc;
import static net.minecraft.client.gui.Gui.drawRect;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {

    @Shadow public int scrollPos;
    @Final @Shadow public java.util.List<ChatLine> drawnChatLines;
    @Shadow public boolean isScrolled;
    @Shadow public abstract int getLineCount();
    @Shadow public abstract boolean getChatOpen();
    @Shadow public abstract float getChatScale();
    @Shadow public abstract int getChatWidth();

    @Inject(method = "drawChat", at = @At(value = "HEAD"), cancellable = true)
    private void drawChat(int updateCounter, CallbackInfo ci) {
        ci.cancel();

        if (mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = this.getLineCount();
            int j = this.drawnChatLines.size();
            float f = mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (j > 0) {
                boolean flag = this.getChatOpen();

                float f1 = this.getChatScale();
                int k = MathHelper.ceil((float) this.getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 8.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);
                int l = 0;

                for (int i1 = 0; i1 + this.scrollPos < this.drawnChatLines.size() && i1 < i; ++i1) {
                    ChatLine chatline = this.drawnChatLines.get(i1 + this.scrollPos);

                    if (chatline != null) {
                        int j1 = updateCounter - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag) {
                            double d0 = (double) j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int) (255.0D * d0);

                            if (flag) {
                                l1 = 255;
                            }

                            l1 = (int) ((float) l1 * f);
                            ++l;

                            if (l1 > 3) {
                                GameplayTweaks.drawChat(i1, chatline, l1, k);
                            }
                        }
                    }
                }

                if (flag) {
                    int k2 = mc.fontRenderer.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = j * k2 + j;
                    int i3 = l * k2 + l;
                    int j3 = this.scrollPos * i3 / j;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = this.isScrolled ? 13382451 : 3355562;
                        drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                        drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                    }
                }

                GlStateManager.popMatrix();
            }
        }
    }

    /* IDK WHEN ??
    @Inject(method = "drawChat(I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiNewChat;drawRect(IIIII)V", ordinal = 0, shift = At.Shift.BEFORE), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
    private void drawChat(int updateCounter, CallbackInfo ci, int i, int j, float f, boolean flag, float f1, int k, int l, int i1, ChatLine chatline, int j1, double d0, int l1, int i2, int j2) {
        ci.cancel();

        GameplayTweaks.drawChat(i1, chatline, l1, k);
    }
     */
}
