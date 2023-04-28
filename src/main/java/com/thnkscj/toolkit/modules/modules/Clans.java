package com.thnkscj.toolkit.modules.modules;

import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.EnumSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

public class Clans extends Module {
    public static EnumSetting<Color> typeOfBadge = new EnumSetting<>("Type of badge", "", Color.Normal);

    public Clans() {
        super("Clans", "Clans", Category.CLIENT);

        addSettings(typeOfBadge);
    }

    public static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-viewerYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((float) (isThirdPersonFrontal ? -1 : 1) * viewerPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(-0.025F, -0.025F, 0.025F);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);

        if (!isSneaking) {
            GlStateManager.disableDepth();
        }

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        int i = fontRendererIn.getStringWidth(str) / 2;
        GlStateManager.disableTexture2D();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos((double) (-i - 1) - 12 + 6, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (-i - 1) - 12 + 6, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1) + 6, 8 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
        bufferbuilder.pos((double) (i + 1) + 6, -1 + verticalShift, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();

        tessellator.draw();
        GlStateManager.enableTexture2D();
        fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2 + 6, verticalShift, 553648127);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);

        if (!isSneaking) {
            fontRendererIn.drawString(str, -fontRendererIn.getStringWidth(str) / 2 + 6, verticalShift, -1);
            GlStateManager.enableDepth();
        }

        Minecraft mc = Minecraft.getMinecraft();
        mc.getTextureManager().bindTexture(new ResourceLocation("icons/badge-" + getBadgeColor(str) + ".png"));
        Gui.drawModalRectWithCustomSizedTexture(-fontRendererIn.getStringWidth(str) / 2 - 12 + 7, -1, 9, 9, 9, 9, 9, 9);

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static String getBadgeColor(String username) {
        return "normal";
    }

    public enum Color {
        Gold("gold"), // Elder & Noble
        Purple("purple"), // Developer/ CJ
        Blue("blue"), // Knight
        Green("green"), // ?
        Orange("orange"), // ?
        Normal("normal"), // Men at Arms
        Mason("mason"), // Mason
        Collective("collective"), // Collective
        Sync("sync"), // Sync
        Astral("astral"), // Astral
        BSB("bsb"), // BSB
        ;

        public final String label;

        Color(String label) {
            this.label = label;
        }
    }
}
