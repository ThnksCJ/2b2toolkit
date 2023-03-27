package com.thnkscj.toolkit.gui;

import com.thnkscj.toolkit.gui.Font.FontRenderer;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.ClickGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

public class GUIUtils extends Gui {

    public static FontRenderer renderer;
    public static FontRenderer smallRenderer;

    static {
        //   renderer = FontRenderer.create(ModuleManager.getModule(ClickGui.class).font.getValueName(), 35, true, true, true);
        //   smallRenderer = FontRenderer.create("Courier New", 18, true, true, true);
    }

    public static void updateFont() {
        renderer = FontRenderer.create(ModuleManager.getModule(ClickGui.class).font.getValueName() + ".ttf", 18, false, false, false);
    }

    public static void glScissor(float x, float y, float x1, float y1, final ScaledResolution sr) {
        GL11.glScissor((int) (x * sr.getScaleFactor()), (int) (Minecraft.getMinecraft().displayHeight - (y1 * sr.getScaleFactor())), (int) ((x1 - x) * sr.getScaleFactor()), (int) ((y1 - y) * sr.getScaleFactor()));
    }

    public static void drawRectangle(float f, float f2, float f3, float f4, int n) {
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        f3 = f + f3;
        f4 = f2 + f4;

        float f5;
        if (f < f3) {
            f5 = f;
            f = f3;
            f3 = f5;
        }
        if (f2 < f4) {
            f5 = f2;
            f2 = f4;
            f4 = f5;
        }
        f5 = (float) (n >> 24 & 0xFF) / 255.0f;
        float f6 = (float) (n >> 16 & 0xFF) / 255.0f;
        float f7 = (float) (n >> 8 & 0xFF) / 255.0f;
        float f8 = (float) (n & 0xFF) / 255.0f;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f6, f7, f8, f5);
        bufferBuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferBuilder.pos(f, f4, 0.0).endVertex();
        bufferBuilder.pos(f3, f4, 0.0).endVertex();
        bufferBuilder.pos(f3, f2, 0.0).endVertex();
        bufferBuilder.pos(f, f2, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();


    }


    public static void drawUnfilledCircle(float x, float y, float radius, int color, float width) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glLineWidth(width);
        GL11.glBegin(GL11.GL_LINE_LOOP);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin((double) i * 3.141526 / 180.0) * (double) radius, y + Math.cos((double) i * 3.141526 / 180.0) * (double) radius);
        }
        GL11.glEnd();
        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GL11.glColor4f(red, green, blue, alpha);
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + Math.sin((double) i * 3.141526 / 180.0) * (double) radius, y + Math.cos((double) i * 3.141526 / 180.0) * (double) radius);
        }
        GL11.glEnd();
        GlStateManager.resetColor();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void circle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 0.0f, 360.0f, radius, fill);
    }

    public static void topCircle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 0.0f, 180.0f, radius, fill);
    }

    public static void bottomCircle(final float x, final float y, final float radius, final int fill) {
        arc(x, y, 180.0f, 360.0f, radius, fill);
    }

    public static void arc(final float x, final float y, final float start, final float end, final float radius,
                           final int color) {
        arcEllipse(x, y, start, end, radius, radius, color);
    }

    public static void arcEllipse(final float x, final float y, float start, float end, final float w, final float h,
                                  final int color) {
        GlStateManager.color(0.0f, 0.0f, 0.0f);
        GL11.glColor4f(0.0f, 0.0f, 0.0f, 0.0f);
        float temp = 0.0f;
        if (start > end) {
            temp = end;
            end = start;
            start = temp;
        }
        final float var11 = (color >> 24 & 0xFF) / 255.0f;
        final float var12 = (color >> 16 & 0xFF) / 255.0f;
        final float var13 = (color >> 8 & 0xFF) / 255.0f;
        final float var14 = (color & 0xFF) / 255.0f;
        final Tessellator var15 = Tessellator.getInstance();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var12, var13, var14, var11);
        if (var11 > 0.5f) {
            glEnable(2848);
            GL11.glLineWidth(2.0f);
            GL11.glBegin(3);
            for (float i = end; i >= start; i -= 4.0f) {
                final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w * 1.001f;
                final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h * 1.001f;
                GL11.glVertex2f(x + ldx, y + ldy);
            }
            GL11.glEnd();
            glDisable(2848);
        }
        GL11.glBegin(6);
        for (float i = end; i >= start; i -= 4.0f) {
            final float ldx = (float) Math.cos(i * 3.141592653589793 / 180.0) * w;
            final float ldy = (float) Math.sin(i * 3.141592653589793 / 180.0) * h;
            GL11.glVertex2f(x + ldx, y + ldy);
        }
        GL11.glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawImage(ResourceLocation resourceLocation, int x, int y, int width, int height) {
        GlStateManager.enableAlpha();
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
        GlStateManager.color(1f, 1f, 1f);
        GL11.glPushMatrix();
        GUIUtils.drawScaledCustomSizeModalRect(x, y, 0, 0, width, height, width, height, width, height);
        GL11.glPopMatrix();
        GlStateManager.disableAlpha();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        right = right + left;
        bottom = bottom + top;
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f5, f6, f7, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawSidewaysGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
        right = right + left;
        bottom = bottom + top;
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(right, top, 0).color(f1, f2, f3, f).endVertex();
        bufferbuilder.pos(left, top, 0).color(f4, f5, f6, f4).endVertex();
        bufferbuilder.pos(left, bottom, 0).color(f4, f5, f6, f4).endVertex();
        bufferbuilder.pos(right, bottom, 0).color(f1, f2, f3, f).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawLine(float x, float y, float x1, float y1, int hex) {
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        float alpha = (hex >> 24 & 0xFF) / 255.0F;

        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.shadeModel(GL_SMOOTH);
        glLineWidth(2);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, y, 0).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x1, y1, 0).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawTriangle(float x, float y, float size, float theta, int color) {
        GL11.glTranslated(x, y, 0);
        GL11.glRotatef(180 + theta, 0F, 0F, 1.0F);

        drawLine(0, (size), (1 * size), -(size), color);
        drawLine(0, (size), -(1 * size), -(size), color);

        GL11.glRotatef(-180 - theta, 0F, 0F, 1.0F);
        GL11.glTranslated(-x, -y, 0);
    }

    public static void drawImages(final int x, final int y, final float u, final float v, final int uWidth, final int vHeight, final int width, final int height, final float tileWidth, final float tileHeight, final ResourceLocation rsc) {
        try {
            Minecraft.getMinecraft().getTextureManager().bindTexture(rsc);
            GL11.glEnable(3042);
            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
            Gui.drawScaledCustomSizeModalRect(x, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
            GL11.glDisable(3042);
        } catch (Exception ex) {
        }
    }


}
