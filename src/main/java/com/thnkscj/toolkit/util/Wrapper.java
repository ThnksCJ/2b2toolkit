package com.thnkscj.toolkit.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

public class Wrapper {


    public static Minecraft mc = Minecraft.getMinecraft();

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return getMinecraft().player;
    }

    public static Entity getRenderEntity() {
        return Wrapper.mc.getRenderViewEntity();
    }

    public static World getWorld() {
        return getMinecraft().world;
    }

    public static int getKey(String keyname) {
        return Keyboard.getKeyIndex(keyname.toUpperCase());
    }


}