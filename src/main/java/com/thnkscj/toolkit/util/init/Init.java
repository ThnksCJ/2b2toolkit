package com.thnkscj.toolkit.util.init;

import com.thnkscj.toolkit.Toolkit;
import com.thnkscj.toolkit.command.CommandManager;
import com.thnkscj.toolkit.config.SaveLoad;
import com.thnkscj.toolkit.event.EventManger;
import com.thnkscj.toolkit.manager.FriendManager;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.modules.modules.ClickGui;

public class Init {
    public static final long startTime = System.nanoTime();
    public static Thread thread;
    public static EventManger eventManger;
    public static CommandManager commandManager;
    public static ModuleManager moduleManager;
    public static FriendManager friendManager;
    public static ClickGui clickGui;
    public static SaveLoad saveLoad;

    public static void loadMessage() {
        Toolkit.log.info("************************************");
        Toolkit.log.info("*     Initialisation Started!      *");
        Toolkit.log.info("************************************");
    }

    public static void loadManagers() {
        thread = new Thread(() -> {
            moduleManager = new ModuleManager();
            commandManager = new CommandManager();
            friendManager = new FriendManager();
            eventManger = new EventManger();
            clickGui = new ClickGui();

            saveLoad = new SaveLoad();

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                SaveLoad.saveConfig();
                Toolkit.log.info("************************************");
                Toolkit.log.info("*         Configs Saved!           *");
                Toolkit.log.info("************************************");
            }));

            SaveLoad.loadConfig();
            Toolkit.log.info("************************************");
            Toolkit.log.info("*      Configs Initialised!        *");
            Toolkit.log.info("************************************");

            Toolkit.log.info("************************************");
            Toolkit.log.info("*      Managers Initialised!       *");
            Toolkit.log.info("************************************");
        });
        thread.start();
    }

    public static void loadMisc() {
        thread = new Thread(() -> {
            CommandManager.initCommands();

            eventManger = new EventManger();
            clickGui = new ClickGui();

            Toolkit.log.info("************************************");
            Toolkit.log.info("*       Misc Initialised!          *");
            Toolkit.log.info("************************************");
        });
        thread.start();
    }
}


