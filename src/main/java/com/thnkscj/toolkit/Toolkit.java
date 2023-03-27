package com.thnkscj.toolkit;

import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.util.init.Init;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

@Mod(modid = Toolkit.MOD_ID, name = Toolkit.MOD_NAME, version = Toolkit.VERSION)
public class Toolkit {

    public static final String MOD_ID = "2b2toolkit";
    public static final String MOD_NAME = "2b2toolkit";
    public static final String VERSION = "v0.3";

    public static final Logger log = LogManager.getLogger(Toolkit.MOD_NAME);

    @Mod.Instance(Toolkit.MOD_ID)
    public static Toolkit INSTANCE;

    @Mod.EventHandler
    public static void init(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(INSTANCE);
        Init.loadMessage();
        Init.loadManagers();
        Init.loadMisc();
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if (Minecraft.getMinecraft().world == null || Minecraft.getMinecraft().player == null)
            return;

        try {
            if (Keyboard.isCreated()) {
                if (Keyboard.getEventKeyState()) {
                    int keycode = Keyboard.getEventKey();
                    if (keycode <= 0)
                        return;
                    for (Module m : ModuleManager.mods) {
                        if (m.getBind() == keycode) {
                            m.toggle();
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
