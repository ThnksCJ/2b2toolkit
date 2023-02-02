package com.cj.toolkit.modules.modules;

import com.cj.toolkit.modules.Category;
import com.cj.toolkit.modules.Module;
import com.cj.toolkit.setting.settings.BooleanSetting;
import com.cj.toolkit.setting.settings.IntegerSetting;
import com.cj.toolkit.util.entity.PlayerUtil;
import kaptainwutax.biomeutils.source.BiomeSource;
import kaptainwutax.mcutils.state.Dimension;
import kaptainwutax.mcutils.version.MCVersion;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class AntiLeak extends Module {

    public AntiLeak() {
        super("AntiLeak", "Dont leak ur base cords and stuff lol", Category.CLIENT);

        addSettings(textureRot, fthreeHidden, terrainHidden, terrainRenderCutoff);
    }

    static long worldSeedOverworld = -4172144997902289642L;
    static long worldSeedNether = 146008555100680L;
    int lastDim;
    static MCVersion version = MCVersion.v1_12_2;
    public static Dimension dimension = Dimension.OVERWORLD;
    public static BiomeSource biomeSource = BiomeSource.of(dimension, version, worldSeedOverworld);

    public static BlockPos offsetRand = new BlockPos(69, 69, 69);

    public static BooleanSetting textureRot = new BooleanSetting("Texture Rotation", "Hide texture rotation", false);
    public static BooleanSetting fthreeHidden = new BooleanSetting("F3 Hidden", "Hide F3", false);
    public static BooleanSetting terrainHidden = new BooleanSetting("Terrain Hidden", "Hide terrain", false);
    public static IntegerSetting terrainRenderCutoff = new IntegerSetting("Terrain Render Cutoff", "Cutoff for terrain rendering", 5, 40, 100);

    private boolean isTextureRot = false;
    private boolean isTerrainHidden = false;
    private int isTerrainRenderCutoff = terrainRenderCutoff.getValue();

    @Override
    public void onEnable() {
        if(PlayerUtil.nullcheck()) return;

        sync();
        reload(true);
    }

    @Override
    public void onDisable() {
        reload(true);
    }

    @Override
    public void onUpdate() {
        if(mc.player.dimension != lastDim) {
            lastDim = mc.player.dimension;
            sync();
        }

        if(terrainHidden.getValue() != isTerrainHidden) {
            isTerrainHidden = terrainHidden.getValue();
            reload(true);
        } else if(textureRot.getValue() != isTextureRot) {
            isTextureRot = textureRot.getValue();
            reload(true);
        } else if(terrainRenderCutoff.getValue() != isTerrainRenderCutoff) {
            isTerrainRenderCutoff = terrainRenderCutoff.getValue();
            reload(true);
        }
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

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onDebugRender(RenderGameOverlayEvent event) {
        if (fthreeHidden.getValue()) {
            if (event.getType() != null && event.getType().equals(RenderGameOverlayEvent.ElementType.DEBUG)) {
                event.setCanceled(true);
            }
        }
    }

    public static void sync() {
        if(PlayerUtil.nullcheck())
            return;

        if (mc.player.dimension == 0) {
            biomeSource = BiomeSource.of(dimension, version, worldSeedOverworld);
        } else {
            biomeSource = BiomeSource.of(dimension, version, worldSeedNether);
        }
    }

    @SubscribeEvent
    public void switchDimension(TickEvent.ClientTickEvent event) {
        if (PlayerUtil.nullcheck())
            return;

        if (mc.player.dimension != lastDim) {
            lastDim = mc.player.dimension;
            sync();
        }
    }
}
