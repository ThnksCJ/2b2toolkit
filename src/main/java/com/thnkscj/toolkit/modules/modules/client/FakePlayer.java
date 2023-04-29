package com.thnkscj.toolkit.modules.modules.client;

import com.mojang.authlib.GameProfile;
import com.thnkscj.toolkit.modules.Category;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.util.entity.PlayerUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

public class FakePlayer extends Module {
    private EntityOtherPlayerMP fakePlayer = null;

    public FakePlayer() {
        super("FakePlayer", "Blah", Category.CLIENT);
    }

    @Override
    public void onEnable() {
        if (PlayerUtil.nullcheck()) {
            disable();
            return;
        }

        new Thread(() -> {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString("9d942108-9c0d-41ed-ac5c-c3ef7353e7c5"), "Thnks_CJ#5607"));

            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.rotationYawHead = mc.player.rotationYaw;

            fakePlayer.setSneaking(mc.player.isSneaking());
            fakePlayer.setPrimaryHand(mc.player.getPrimaryHand());

            //fakePlayer.setSneaking(true);

            mc.world.addEntityToWorld(-420, fakePlayer);
        }).start();
    }

    @Override
    public void onDisable() {
        if (fakePlayer != null) {
            if (mc.world.removeEntityFromWorld(-420) != null) {
                mc.world.removeEntityFromWorld(-420);
            }
            fakePlayer = null;
        }
    }
}
