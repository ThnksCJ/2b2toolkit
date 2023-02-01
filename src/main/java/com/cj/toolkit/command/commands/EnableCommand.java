package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.modules.ModuleManager;

public class EnableCommand extends Command {
    public EnableCommand() {
        super("Enable");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"enable", "toggle", "e"};
    }

    @Override
    public String getSyntax() {
        return "enable <Module>";
    }

    @Override
    public void onCommand(String command, String[] args) {
        if (ModuleManager.getModuleName(args[0]) != null) {
            if (!ModuleManager.getModuleName(args[0]).isEnabled()) {
                ModuleManager.getModuleName(args[0]).enable();
            } else {
                Command.sendMessage("Module " + args[0] + " is already enabled");
            }
        } else {
            Command.sendMessage("Could not find module with name: " + args[0] + ".");
        }
    }

}
