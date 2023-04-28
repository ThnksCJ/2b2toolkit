package com.thnkscj.toolkit.modules.modules;

import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.setting.settings.BooleanSetting;
import com.thnkscj.toolkit.util.render.RenderUtil;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.thnkscj.toolkit.util.render.RenderUtil.drawRect;

public class GameplayTweaks extends Module {
    public static BooleanSetting showRedstonePowered = new BooleanSetting("ShowRedstonePowered", "Show redstone powered blocks", true);
    public static BooleanSetting showMsgIcons = new BooleanSetting("Message Icons", "Show icons next to messages in chat", true);

    public GameplayTweaks() {
        super("GameplayTweaks", "Add various tweaks to gameplay", Category.CLIENT);

        addSettings(showMsgIcons, showRedstonePowered);
    }

    public static void drawChat(int i1, ChatLine chatline, int l1, int k) {
        String s = chatline.getChatComponent().getFormattedText();

        GlStateManager.enableBlend();
        drawRect(-2, -i1 * 9 - 9, 9 + (k + 4), -i1 * 9, 0x7F000000);

        for (String part : s.split("(§.)|[^\\w]")) {
            if (part.isEmpty()) continue;
            if (!ModuleManager.getModule(GameplayTweaks.class).isEnabled()) continue;

            if (mc.getConnection().getPlayerInfoMap().stream().anyMatch(p -> p.getGameProfile().getName().equalsIgnoreCase(part))) {
                NetworkPlayerInfo p = mc.getConnection().getPlayerInfo(part);

                if (p != null) {
                    mc.getTextureManager().bindTexture(p.getLocationSkin());

                    float y = (float) (((float) -i1 * 9) - 8.5);
                    float f = 1.0F / 64.0F;
                    float f1 = 1.0F / 64.0F;

                    Tessellator tessellator = Tessellator.getInstance();
                    BufferBuilder bufferbuilder = tessellator.getBuffer();
                    bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                    bufferbuilder.pos(0, y + 8, 0.0D).tex(8.0F * f, (8.0F + 8) * f1).endVertex();
                    bufferbuilder.pos(8, y + 8, 0.0D).tex((8.0F + 8) * f, (8.0F + 8) * f1).endVertex();
                    bufferbuilder.pos(8, y, 0.0D).tex((8.0F + 8) * f, 8.0F * f1).endVertex();
                    bufferbuilder.pos(0, y, 0.0D).tex(8.0F * f, 8.0F * f1).endVertex();
                    tessellator.draw();

                    GlStateManager.enableBlend();
                    mc.fontRenderer.drawStringWithShadow(s, 10.0F, (float) (-i1 * 9 - 8), 16777215 + (l1 << 24));
                    GlStateManager.disableAlpha();
                    GlStateManager.disableBlend();
                }
                return;
            } else if (part.equals("2b2toolkit")) {
                mc.getTextureManager().bindTexture(new ResourceLocation("icons/2b2toolkit.png"));

                float y = (float) (((float) -i1 * 9) - 8.5);
                float f = 1.0F / 64.0F;
                float f1 = 1.0F / 64.0F;

                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferbuilder = tessellator.getBuffer();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
                bufferbuilder.pos(0, y + 8, 0.0D).tex(8.0F * f, (8.0F + 8) * f1).endVertex();
                bufferbuilder.pos(8, y + 8, 0.0D).tex((8.0F + 8) * f, (8.0F + 8) * f1).endVertex();
                bufferbuilder.pos(8, y, 0.0D).tex((8.0F + 8) * f, 8.0F * f1).endVertex();
                bufferbuilder.pos(0, y, 0.0D).tex(8.0F * f, 8.0F * f1).endVertex();
                tessellator.draw();

                GlStateManager.enableBlend();
                mc.fontRenderer.drawStringWithShadow(s, 10.0F, (float) (-i1 * 9 - 8), 16777215 + (l1 << 24));
                GlStateManager.disableAlpha();
                GlStateManager.disableBlend();
                return;
            }
        }

        GlStateManager.enableBlend();
        mc.fontRenderer.drawStringWithShadow(s, 0.0F, (float) (-i1 * 9 - 8), 16777215 + (l1 << 24));
        GlStateManager.disableAlpha();
        GlStateManager.disableBlend();
    }

    @Override
    public void onWorldRender(RenderWorldLastEvent event) {
        if (showRedstonePowered.getValue())
            showRedstonePowered.setValue(false);

        List<BlockPos> blocks = new ArrayList<>();

        for (int x = (int) mc.player.posX - 10; x < (int) mc.player.posX + 10; x++)
            for (int y = (int) mc.player.posY - 10; y < (int) mc.player.posY + 10; y++)
                for (int z = (int) mc.player.posZ - 10; z < (int) mc.player.posZ + 10; z++)
                    blocks.add(new BlockPos(x, y, z));

        if (showRedstonePowered.getValue()) {
            for (BlockPos block : blocks) {
                if (
                        mc.world.isBlockPowered(block) &&
                                !mc.world.getBlockState(block).getBlock().getRegistryName().toString().contains("air") &&
                                !mc.world.getBlockState(block).getBlock().getRegistryName().toString().contains("water") &&
                                !mc.world.getBlockState(block).getBlock().getRegistryName().toString().contains("lava") &&
                                !mc.world.getBlockState(block).getBlock().getRegistryName().toString().contains("repeater")
                ) {
                    RenderUtil.drawBox2(block, new Color(255, 0, 0, 40));
                }
            }
        }
    }
}
