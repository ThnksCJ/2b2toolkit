package com.thnkscj.toolkit.mixin.mixins;

import com.thnkscj.toolkit.mixin.Precedence;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.client.TNTHelper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderTNTPrimed;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityTNTPrimed;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.text.DecimalFormat;

import static com.thnkscj.toolkit.util.render.RenderUtil.mc;

@Mixin(value = RenderTNTPrimed.class, priority = Precedence.NORMAL)
public class MixinRenderTNTPrimed {
    @Inject(method = "doRender*", at = @At("RETURN"))
    private void insertCode(EntityTNTPrimed entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        if (ModuleManager.getModule(TNTHelper.class).isEnabled() && TNTHelper.renderTag.getValue())
            renderTag(RenderTNTPrimed.class.cast(this), entity, x, y, z, partialTicks);
    }

    public void renderTag(RenderTNTPrimed tntRenderer, EntityTNTPrimed tntPrimed, double x, double y, double z, float partialTicks) {
        final int fuseTimer = tntPrimed.getFuse();
        if (fuseTimer < 1) return;
        double distance = tntPrimed.getDistanceSq(tntRenderer.getRenderManager().renderViewEntity);
        DecimalFormat timeFormatter = new DecimalFormat("0.00");

        if (distance <= 4096D) {
            float number = (fuseTimer - partialTicks) / 20F;
            String time = timeFormatter.format(number);
            FontRenderer fontrenderer = tntRenderer.getFontRendererFromRenderManager();
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + tntPrimed.height + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-tntRenderer.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);

            int xMultiplier = 1; // Nametag x rotations should flip in front-facing 3rd person

            if (mc.gameSettings.thirdPersonView == 2) {
                xMultiplier = -1;
            }

            float scale = 0.02666667f;
            GlStateManager.rotate(tntRenderer.getRenderManager().playerViewX * xMultiplier, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-scale, -scale, scale);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            int stringWidth = fontrenderer.getStringWidth(time) >> 1;
            float green = Math.min(fuseTimer / 80f, 1f);
            Color color = new Color(1f - green, green, 0f);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            GlStateManager.disableTexture2D();
            tessellator.getBuffer().begin(7, DefaultVertexFormats.POSITION_COLOR);
            tessellator.getBuffer().pos(-stringWidth - 1, -1.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 0.25F).endVertex();
            tessellator.getBuffer().pos(-stringWidth - 1, 8.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 0.25F).endVertex();
            tessellator.getBuffer().pos(stringWidth + 1, 8.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 0.25F).endVertex();
            tessellator.getBuffer().pos(stringWidth + 1, -1.0D, 0.0D).color(color.getRed(), color.getGreen(), color.getBlue(), 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(time, -fontrenderer.getStringWidth(time) >> 1, 0, color.getRGB());
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }
}
