package com.thnkscj.toolkit.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Quaternion;

import java.awt.*;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

public class RenderUtil {
    public static RenderUtil INSTANCE;
    public static Tessellator tessellator;
    public static BufferBuilder builder;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static int[][][] cubletLookup = {
            {
                    {17, 9, 0},
                    {20, 16, 3},
                    {23, 15, 6}
            },
            {
                    {18, 10, 1},
                    {21, -1, 4},
                    {24, 14, 7}
            },
            {
                    {19, 11, 2},
                    {22, 12, 5},
                    {25, 13, 8}
            }
    };
    public static int[][] cubeSides = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8},
            {19, 18, 17, 22, 21, 20, 25, 24, 23},
            {0, 1, 2, 9, 10, 11, 17, 18, 19},
            {23, 24, 25, 15, 14, 13, 6, 7, 8},
            {17, 9, 0, 20, 16, 3, 23, 15, 6},
            {2, 11, 19, 5, 12, 22, 8, 13, 25}
    };
    public static int[][] cubeSideTransforms = {
            {0, 0, 1},
            {0, 0, -1},
            {0, 1, 0},
            {0, -1, 0},
            {-1, 0, 0},
            {1, 0, 0}
    };
    public static Quaternion[] cubeletStatus = {new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion(),
            new Quaternion()
    };

    static {
        tessellator = Tessellator.getInstance();
        builder = RenderUtil.tessellator.getBuffer();
    }

    static {
        RenderUtil.INSTANCE = new RenderUtil();
    }

    public final BufferBuilder buffer;
    public final WorldVertexBufferUploader vboUploader;

    public RenderUtil() {
        this.vboUploader = new WorldVertexBufferUploader();
        this.buffer = new BufferBuilder(2097152);
    }

    public static BufferBuilder getBuffer() {
        return INSTANCE.buffer;
    }

    public static void glColor(int hex) {
        float alpha = (hex >> 24 & 0xFF) / 255.0F;
        float red = (hex >> 16 & 0xFF) / 255.0F;
        float green = (hex >> 8 & 0xFF) / 255.0F;
        float blue = (hex & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void glBillboardDistanceScaled(float x, float y, float z, EntityPlayer player, float scale) {
        glBillboard(x, y, z);
        int distance = (int) player.getDistance(x, y, z);
        float scaleDistance = (distance / 2.0f) / (2.0f + (2.0f - scale));
        if (scaleDistance < 1f)
            scaleDistance = 1;
        GlStateManager.scale(scaleDistance, scaleDistance, scaleDistance);
    }

    public static void glBillboard(float x, float y, float z) {
        float scale = 0.016666668f * 1.6f;
        GlStateManager.translate(x - Minecraft.getMinecraft().getRenderManager().renderPosX, y - Minecraft.getMinecraft().getRenderManager().renderPosY, z - Minecraft.getMinecraft().getRenderManager().renderPosZ);
        GlStateManager.glNormal3f(0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-Minecraft.getMinecraft().player.rotationYaw, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(Minecraft.getMinecraft().player.rotationPitch, Minecraft.getMinecraft().gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f);
        GlStateManager.scale(-scale, -scale, scale);
    }

    public static void drawCircle2D(final float x, final float y, final float radius, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        GL11.glColor4d(getRedFromHex(color), getGreenFromHex(color), getBlueFromHex(color), getAlphaFromHex(color));
        GL11.glBegin(9);
        for (int i = 0; i <= 360; ++i) {
            GL11.glVertex2d(x + MathHelper.sin(i * Float.intBitsToFloat(Float.floatToIntBits(0.30120212f) ^ 0x7ED338F1) / Float.intBitsToFloat(Float.floatToIntBits(0.07748035f) ^ 0x7EAAAE05)) * radius, y + MathHelper.cos(i * Float.intBitsToFloat(Float.floatToIntBits(0.6391426f) ^ 0x7F6A9102) / Float.intBitsToFloat(Float.floatToIntBits(0.011713186f) ^ 0x7F0BE8AA)) * radius);
        }
        GL11.glColor4d(Double.longBitsToDouble(Double.doubleToLongBits(53.67714277800314) ^ 0x7FBAD6AC9D531F7FL), Double.longBitsToDouble(Double.doubleToLongBits(501.6963415926147) ^ 0x7F8F5B243714F1FFL), Double.longBitsToDouble(Double.doubleToLongBits(6.706577014744621) ^ 0x7FEAD388ECC9BBDCL), Double.longBitsToDouble(Double.doubleToLongBits(14.139351235344405) ^ 0x7FDC47590B8CEC3FL));
        GL11.glEnd();
        GlStateManager.shadeModel(7424);
        GL11.glDisable(2848);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    public static double getAlphaFromHex(final int color) {
        return (color >> 24 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.008373946f) ^ 0x7F7632E0);
    }

    public static double getRedFromHex(final int color) {
        return (color >> 16 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.032228123f) ^ 0x7E7B01A3);
    }

    public static double getGreenFromHex(final int color) {
        return (color >> 8 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.0140558975f) ^ 0x7F194AB5);
    }

    public static double getBlueFromHex(final int color) {
        return (color & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.009096651f) ^ 0x7F6A0A1F);
    }

    public static void drawPreciseRectangle(double left, double top, double right, double bottom, final int color) {
        if (left < right) {
            final double i = left;
            left = right;
            right = i;
        }
        if (top < bottom) {
            final double j = top;
            top = bottom;
            bottom = j;
        }
        final float f3 = (color >> 24 & 0xFF) / 255.0f;
        final float f4 = (color >> 16 & 0xFF) / 255.0f;
        final float f5 = (color >> 8 & 0xFF) / 255.0f;
        final float f6 = (color & 0xFF) / 255.0f;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.color(f4, f5, f6, f3);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos(left, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, bottom, 0.0).endVertex();
        bufferbuilder.pos(right, top, 0.0).endVertex();
        bufferbuilder.pos(left, top, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void glColor(int redRGB, int greenRGB, int blueRGB, int alphaRGB) {
        float red = 0.003921569F * redRGB;
        float green = 0.003921569F * greenRGB;
        float blue = 0.003921569F * blueRGB;
        float alpha = 0.003921569F * alphaRGB;
        GL11.glColor4f(red, green, blue, alpha);
    }

    public static void prepare() {
        glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ZERO, GL11.GL_ONE);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.depthMask(false);
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.enableAlpha();
        glEnable(GL11.GL_LINE_SMOOTH);
        glEnable(GL32.GL_DEPTH_CLAMP);
    }

    public static void release() {
        GL11.glDisable(GL32.GL_DEPTH_CLAMP);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GlStateManager.enableAlpha();
        GlStateManager.enableCull();
        GlStateManager.enableTexture2D();
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
        GlStateManager.glLineWidth(1.0f);
        GlStateManager.shadeModel(GL11.GL_FLAT);
        glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }

    public static void drawRect(float x, float y, float w, float h, int color) {
        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(x, h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(w, h, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(w, y, 0.0D).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float left, float top, float right, float bottom, int startColor, int endColor) {
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

    public static void drawRectangleBordered(double x, double y, double x1, double y1, double width, int internalColor, int external) {
        drawPreciseRectangle(x + width, y + width, x1 - width, y1 - width, internalColor);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawPreciseRectangle(x + width, y, x1 - width, y + width, external);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawPreciseRectangle(x, y, x + width, y1, external);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawPreciseRectangle(x1 - width, y, x1, y1, external);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        drawPreciseRectangle(x + width, y1 - width, x1 - width, y1, external);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    }

    public static void drawTriangle(float x, float y, float size, float rotation, int color) {
        GL11.glTranslated(x, y, 0);
        GL11.glRotatef(180 + rotation, 0F, 0F, 1.0F);

        float alpha = (float) (color >> 24 & 255) / 255.0F;
        float red = (float) (color >> 16 & 255) / 255.0F;
        float green = (float) (color >> 8 & 255) / 255.0F;
        float blue = (float) (color & 255) / 255.0F;

        GL11.glColor4f(red, green, blue, alpha);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(1);
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

        GL11.glVertex2d(0, (size));
        GL11.glVertex2d((1 * size), -(size));
        GL11.glVertex2d(-(1 * size), -(size));

        GL11.glEnd();
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glRotatef(-180 - rotation, 0F, 0F, 1.0F);
        GL11.glTranslated(-x, -y, 0);
    }

    public static void drawLeftGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(GL_SMOOTH);
        builder.begin(GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        builder.pos(right, top, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, top, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(left, bottom, 0).color((float) (startColor >> 16 & 255) / 255.0F, (float) (startColor >> 8 & 255) / 255.0F, (float) (startColor & 255) / 255.0F, (float) (startColor >> 24 & 255) / 255.0F).endVertex();
        builder.pos(right, bottom, 0).color((float) (endColor >> 24 & 255) / 255.0F, (float) (endColor >> 16 & 255) / 255.0F, (float) (endColor >> 8 & 255) / 255.0F, (float) (endColor >> 24 & 255) / 255.0F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(GL_FLAT);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void RenderLine(List<Vec3d> List, int Color, double width) {

        prepare();
        GL11.glEnable(GL13.GL_MULTISAMPLE);
        GL11.glLineWidth((float) width);
        glColor(Color);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        RenderManager renderManager = mc.getRenderManager();


        for (Vec3d blockPos : List) {
            GL11.glVertex3d(blockPos.x - renderManager.viewerPosX, blockPos.y - renderManager.viewerPosY, blockPos.z - renderManager.viewerPosZ);
        }

        GL11.glEnd();
        release();
        GL11.glDisable(GL13.GL_MULTISAMPLE);
    }

    public static AxisAlignedBB interpolateAxis(AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - RenderUtil.mc.getRenderManager().viewerPosX, bb.minY - RenderUtil.mc.getRenderManager().viewerPosY, bb.minZ - RenderUtil.mc.getRenderManager().viewerPosZ, bb.maxX - RenderUtil.mc.getRenderManager().viewerPosX, bb.maxY - RenderUtil.mc.getRenderManager().viewerPosY, bb.maxZ - RenderUtil.mc.getRenderManager().viewerPosZ);
    }

    public static void drawBox(AxisAlignedBB a, Color boxColor, Color outlineColor, float lineWidth, boolean outline, boolean box, float alpha, float scale, double slab) {
        double f = 0.5 * (1 - scale);
        AxisAlignedBB bb = RenderUtil.interpolateAxis(new AxisAlignedBB(a.minX + f, a.minY + f + (1 - slab), a.minZ + f, a.maxX - f, a.maxY - f, a.maxZ - f));
        float rB = (float) boxColor.getRed() / 255.0f;
        float gB = (float) boxColor.getGreen() / 255.0f;
        float bB = (float) boxColor.getBlue() / 255.0f;
        float aB = (float) boxColor.getAlpha() / 255.0f;
        float rO = (float) outlineColor.getRed() / 255.0f;
        float gO = (float) outlineColor.getGreen() / 255.0f;
        float bO = (float) outlineColor.getBlue() / 255.0f;
        float aO = (float) outlineColor.getAlpha() / 255.0f;

        if (alpha > 1) alpha = 1;
        aB *= alpha;
        aO *= alpha;
        if (box) {
            GlStateManager.disableDepth();
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            RenderGlobal.renderFilledBox(bb, rB, gB, bB, aB);
            GL11.glDisable(2848);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            GlStateManager.enableDepth();
        }
        if (outline) {
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
            GlStateManager.disableTexture2D();
            GL11.glEnable(2848);
            GL11.glHint(3154, 4354);
            GL11.glLineWidth(lineWidth);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(rO, gO, bO, aO).endVertex();
            tessellator.draw();
            GL11.glDisable(2848);
            GlStateManager.enableTexture2D();
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void drawBox2(BlockPos pos, final Color color) {
        final float red = color.getRed() > 1.0 ? color.getRed() / 255.0f : color.getRed();
        final float green = color.getGreen() > 1.0 ? color.getGreen() / 255.0f : color.getGreen();
        final float blue = color.getBlue() > 1.0 ? color.getBlue() / 255.0f : color.getBlue();
        final float alpha = color.getAlpha() > 1.0 ? color.getAlpha() / 255.0f : color.getAlpha();
        final AxisAlignedBB bb = getViewerPos(pos);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        RenderGlobal.renderFilledBox(bb, red, green, blue, alpha);
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static AxisAlignedBB getViewerPos(final BlockPos pos) {
        return new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ, pos.getX() + 1 - mc.getRenderManager().viewerPosX, pos.getY() + 1 - mc.getRenderManager().viewerPosY, pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
    }

    public static void connectPoints(float xOne, float yOne, float xTwo, float yTwo) {
        glPushMatrix();
        glEnable(GL_LINE_SMOOTH);
        glColor4f(1.0F, 1.0F, 1.0F, 0.8F);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_BLEND);
        glLineWidth(0.5F);
        glBegin(GL_LINES);
        glVertex2f(xOne, yOne);
        glVertex2f(xTwo, yTwo);
        glEnd();
        glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        glDisable(GL_LINE_SMOOTH);
        glEnable(GL_TEXTURE_2D);
        glPopMatrix();
    }

    public static void drawCircle(float x, float y, float radius, int color) {
        glColor(color);
        glEnable(GL_BLEND);
        glDisable(GL_TEXTURE_2D);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_LINE_SMOOTH);
        glPushMatrix();
        glLineWidth(1F);
        glBegin(GL_POLYGON);
        for (int i = 0; i <= 360; i++)
            glVertex2d(x + Math.sin(i * Math.PI / 180.0D) * radius, y + Math.cos(i * Math.PI / 180.0D) * radius);
        glEnd();
        glPopMatrix();
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_LINE_SMOOTH);
        glColor4f(1F, 1F, 1F, 1F);
    }

    public static void renderCircle(double x, double y, double radius, int color) {
        renderCircle(x, y, 0, 360, radius - 1, color);
    }

    public static void renderCircle(double x, double y, double start, double end, double radius, int color) {
        renderCircle(x, y, start, end, radius, radius, color);
    }

    public static void renderCircle(double x, double y, double start, double end, double w, double h, int color) {
        GlStateManager.color(0, 0, 0);
        GL11.glColor4f(0, 0, 0, 0);

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        glColor(color);

        quickRenderCircle(x, y, start, end, w, h);

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void quickRenderCircle(double x, double y, double start, double end, double w, double h) {
        if (start > end) {
            double temp = end;
            end = start;
            start = temp;
        }

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2d(x, y);
        for (double i = end; i >= start; i -= 4) {
            double ldx = Math.cos(i * Math.PI / 180.0) * w;
            double ldy = Math.sin(i * Math.PI / 180.0) * h;
            GL11.glVertex2d(x + ldx, y + ldy);
        }
        GL11.glVertex2d(x, y);
        GL11.glEnd();
    }

    public static void drawGradientBlockOutline(AxisAlignedBB bb, Color startColor, Color midColor, Color endColor, float linewidth) {
        float red = (float) endColor.getRed() / 255.0f;
        float green = (float) endColor.getGreen() / 255.0f;
        float blue = (float) endColor.getBlue() / 255.0f;
        float alpha = (float) endColor.getAlpha() / 255.0f;
        float red1 = (float) midColor.getRed() / 255.0f;
        float green1 = (float) midColor.getGreen() / 255.0f;
        float blue1 = (float) midColor.getBlue() / 255.0f;
        float alpha1 = (float) midColor.getAlpha() / 255.0f;
        float red2 = (float) startColor.getRed() / 255.0f;
        float green2 = (float) startColor.getGreen() / 255.0f;
        float blue2 = (float) startColor.getBlue() / 255.0f;
        float alpha2 = (float) startColor.getAlpha() / 255.0f;
        double dif = (bb.maxY - bb.minY) / 2.0;
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(linewidth);
        GL11.glBegin(1);
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY, bb.minZ);
        GL11.glColor4d(red1, green1, blue1, alpha1);
        GL11.glVertex3d(bb.minX, bb.minY + dif, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.minY + dif, bb.minZ);
        GL11.glColor4f(red2, green2, blue2, alpha2);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glVertex3d(bb.minX, bb.minY, bb.maxZ);
        GL11.glColor4d(red1, green1, blue1, alpha1);
        GL11.glVertex3d(bb.minX, bb.minY + dif, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.minY + dif, bb.maxZ);
        GL11.glColor4d(red2, green2, blue2, alpha2);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.maxZ);
        GL11.glColor4d(red1, green1, blue1, alpha1);
        GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.maxZ);
        GL11.glColor4d(red2, green2, blue2, alpha2);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glColor4d(red, green, blue, alpha);
        GL11.glVertex3d(bb.maxX, bb.minY, bb.minZ);
        GL11.glColor4d(red1, green1, blue1, alpha1);
        GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.minY + dif, bb.minZ);
        GL11.glColor4d(red2, green2, blue2, alpha2);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glColor4d(red2, green2, blue2, alpha2);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.maxX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.maxZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glVertex3d(bb.minX, bb.maxY, bb.minZ);
        GL11.glEnd();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    public static void drawCircleLines(final BufferBuilder buffer, final float x, final float y, final float z, final float radius, final int r, final int g, final int b, final int a) {
        for (int i = 0; i < 360; ++i) {
            buffer.pos(x + Math.sin(i * 3.1415926 / 180.0) * radius, y, z + Math.cos(i * 3.1415926 / 180.0) * radius).color(r, g, b, a).endVertex();
            buffer.pos(x + Math.sin((i + 1) * 3.1415926 / 180.0) * radius, y, z + Math.cos((i + 1) * 3.1415926 / 180.0) * radius).color(r, g, b, a).endVertex();
        }
    }

    public static void drawFadeCircleLines(final BufferBuilder buffer, final float x, final float y, final float z, final float radius, final float height, final int circles, final int r, final int g, final int b) {
        final float jump = height / circles;
        final float alpha = 255.0f / circles;
        for (int i = 0; i <= circles; ++i) {
            final int alphaAt = Math.abs(i - circles);
            drawCircleLines(x, y + jump * i, z, radius, r, g, b, (int) (alphaAt * alpha));
        }
    }

    public static void drawFadeCircleLines(final float x, final float y, final float z, final float radius, final float height, final int circles, final int r, final int g, final int b) {
        drawFadeCircleLines(RenderUtil.getBuffer(), x, y, z, radius, height, circles, r, g, b);
    }

    public static void drawCircleLines(final float x, final float y, final float z, final float radius, final int r, final int g, final int b, final int a) {
        drawCircleLines(getBuffer(), x, y, z, radius, r, g, b, a);
    }

    public static void drawESP(AxisAlignedBB bb, Color color) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glColor4f(color.getRed() / 255, color.getGreen() / 255, color.getBlue() / 255, color.getAlpha());
        RenderUtil.drawBoxForEsp(bb);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public static void drawBoxForEsp(AxisAlignedBB boundingBox) {
        if (boundingBox == null) {
            return;
        }

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.maxY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();

        GlStateManager.glBegin(GL11.GL_QUADS);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.minZ);
        GlStateManager.glVertex3f((float) boundingBox.minX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glVertex3f((float) boundingBox.maxX, (float) boundingBox.minY, (float) boundingBox.maxZ);
        GlStateManager.glEnd();
    }

    public static double easeInOutCubic(double t) {
        return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
    }

    public static void begin(final int mode) {
        getBuffer().begin(mode, DefaultVertexFormats.POSITION_COLOR);
    }

    public void draw() {
        this.buffer.finishDrawing();
        this.vboUploader.draw(this.buffer);
    }

    public enum preparemode {
        QUADS,
        LINES
    }

    public enum Sides {
        ALL,
        UP,
        DOWN,
        NORTH,
        SOUTH,
        WEST,
        EAST
    }
}
