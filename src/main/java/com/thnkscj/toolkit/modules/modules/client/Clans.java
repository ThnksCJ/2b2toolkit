package com.thnkscj.toolkit.modules.modules.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.EnumSetting;
import com.thnkscj.toolkit.util.misc.HttpUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class Clans extends Module {
    public static EnumSetting<ClanType> typeOfBadge = new EnumSetting<>("Type of badge", "", ClanType.Normal);

    public Clans() {
        super("Clans", "Clans", Category.CLIENT);

        addSettings(typeOfBadge);
    }

    public static Map<String, String> clanCache = new HashMap<>();
    public static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    @Override
    public void onEnable() {
        clanCache.clear();
        clanCache.putAll(gson.fromJson(HttpUtil.getResponse("https://pastebin.com/raw/0vf4fHNc"), Map.class));
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

        String badge = getBadgeColor(str);

        if (!badge.equals("blank")) {
            mc.getTextureManager().bindTexture(new ResourceLocation("icons/badge-" + badge + ".png"));
            Gui.drawModalRectWithCustomSizedTexture(-fontRendererIn.getStringWidth(str) / 2 - 12 + 7, -1, 9, 9, 9, 9, 9, 9);
        }

        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static String getBadgeColor(String username) {
        if (clanCache.containsKey(username))
            return clanCache.getOrDefault(username, "blank");

        NetworkPlayerInfo profile = Minecraft.getMinecraft().getConnection().getPlayerInfo(username);

        if (profile == null)
            return "blank";

        String uuid = profile.getGameProfile().getId().toString();
        clanCache.put(username, clanCache.get(uuid));
        clanCache.remove(uuid);

        return clanCache.getOrDefault(username, "blank");
    }

    public enum ClanType {
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
        BLANK("blank");

        public final String label;

        ClanType(String label) {
            this.label = label;
        }
    }
}
