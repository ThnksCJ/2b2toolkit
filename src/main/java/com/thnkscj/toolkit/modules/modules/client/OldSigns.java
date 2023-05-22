package com.thnkscj.toolkit.modules.modules.client;

import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.setting.settings.ColorSetting;
import com.thnkscj.toolkit.setting.settings.IntegerSetting;
import com.thnkscj.toolkit.util.render.RenderUtil;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class OldSigns extends Module {
    public OldSigns() {
        super("OldSigns", "", Category.CLIENT);

        addSettings(color, distance);
    }

    public final ColorSetting color = new ColorSetting("Color", "", new Color(75, 27, 187, 255), true);
    public final IntegerSetting distance = new IntegerSetting("Distance", "", 0, 100, 300);

    @Override
    public void onWorldRender(RenderWorldLastEvent event) {
        if (mc.world.loadedEntityList.isEmpty())
            return;

        mc.world.loadedTileEntityList.forEach(tileEntity -> {
            if (tileEntity instanceof TileEntitySign && mc.player.getDistance(tileEntity.getPos().x, tileEntity.getPos().y, tileEntity.getPos().z) <= distance.getValue() && RenderUtil.isTileEntityVisible(tileEntity)) {
                TileEntitySign sign = (TileEntitySign) tileEntity;
                if (isOldSignText(sign.getPos())) {
                    RenderUtil.outlineBox(new AxisAlignedBB(sign.getPos()), color.getValue(), color.getColor(), 1, 1, 1, 1);
                }
            }
        });
    }

    private boolean isOldSignText(BlockPos pos) {
        // Explanation: Old signs on 2b2t (pre-2015 <1.9 ?) have older style NBT text tags.
        // we can tell them apart by checking if there are siblings in the tag. Old signs won't have siblings.
        TileEntitySign sign;
        if (mc.player.world.getTileEntity(pos) instanceof TileEntitySign) {
            sign = (TileEntitySign) mc.player.world.getTileEntity(pos);
            assert sign != null;
            List<TextComponentString> signTextComponents = Arrays.stream(sign.signText)
                    .filter(component -> component instanceof TextComponentString)
                    .map(component -> (TextComponentString) component)
                    .collect(Collectors.toList());
            AtomicBoolean empty = new AtomicBoolean(false);

            //avoid blank signs
            signTextComponents.forEach(t -> {
                if (t.text.isEmpty()) {
                    empty.set(true);
                }
            });
            return !empty.get() && signTextComponents.stream()
                    .allMatch(component -> component.getSiblings().isEmpty());
        }
        return false;
    }
}
