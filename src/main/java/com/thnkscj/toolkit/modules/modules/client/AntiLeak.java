package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.BooleanSetting;
import com.thnkscj.toolkit.setting.settings.EnumSetting;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import com.thnkscj.toolkit.util.entity.PlayerUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AntiLeak extends Module {
    public static BooleanSetting textureRot = new BooleanSetting("Texture Rotation", "Hide texture rotation", false);
    public static BooleanSetting fthreeHidden = new BooleanSetting("F3 Hidden", "Hide F3", false);
    public static EnumSetting<F3Spoof> fthreeSpoof = new EnumSetting<>("F3 Spoof", "Spoof F3", F3Spoof.Off);
    public static BooleanSetting terrainHidden = new BooleanSetting("Terrain Hidden", "Hide terrain", false);
    public static IntegerSetting terrainRenderCutoff = new IntegerSetting("Terrain Render Cutoff", "Cutoff for terrain rendering", 5, 40, 100);

    public static BlockPos offsetRand = new BlockPos(192172, 69, 278941);
    private boolean isTextureRot = false;
    private boolean isTerrainHidden = false;
    private int isTerrainRenderCutoff = terrainRenderCutoff.getValue();

    public static AntiLeak INSTANCE;

    public AntiLeak() {
        super("AntiLeak", "Dont leak ur base cords and stuff lol", Category.CLIENT);
        addSettings(textureRot, fthreeHidden, fthreeSpoof, terrainHidden, terrainRenderCutoff);
        INSTANCE = this;
    }

    private static void reload(boolean soft) {
        if (soft) {
            int x = (int) mc.player.posX;
            int y = (int) mc.player.posY;
            int z = (int) mc.player.posZ;
            int d = mc.gameSettings.renderDistanceChunks * 16;
            mc.renderGlobal.markBlockRangeForRenderUpdate(x - d, y - d, z - d, x + d, y + d, z + d);
            return;
        }

        mc.renderGlobal.loadRenderers();
    }

    @Override
    public void onEnable() {
        if (PlayerUtil.nullcheck()) return;

        reload(true);
    }

    @Override
    public void onDisable() {
        reload(true);
    }

    @Override
    public void onUpdate() {
        if (terrainHidden.getValue() != isTerrainHidden) {
            isTerrainHidden = terrainHidden.getValue();
            reload(true);
        } else if (textureRot.getValue() != isTextureRot) {
            isTextureRot = textureRot.getValue();
            reload(true);
        } else if (terrainRenderCutoff.getValue() != isTerrainRenderCutoff) {
            isTerrainRenderCutoff = terrainRenderCutoff.getValue();
            reload(true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDebugRender(RenderGameOverlayEvent event) {
        if (fthreeHidden.getValue()) {
            if (event.getType() != null && event.getType().equals(RenderGameOverlayEvent.ElementType.DEBUG)) {
                event.setCanceled(true);
            }
        }
    }

    public enum F3Spoof {
        Off,
        Goofy,
        Compatible
    }
}
