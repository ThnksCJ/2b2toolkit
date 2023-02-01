package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.modules.ModuleManager;

public class DisableCommand extends Command {
    public DisableCommand() {
        super("Disable");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"disable", "d"};
    }

    @Override
    public String getSyntax() {
        return "disable <Module>";
    }

    @Override
    public void onCommand(String command, String[] args) {
        if (ModuleManager.getModuleName(args[0]) != null) {
            if (ModuleManager.getModuleName(args[0]).isEnabled()) {
                ModuleManager.getModuleName(args[0]).disable();
            } else {
                Command.sendMessage("Module " + args[0] + " is already disabled");
            }
        } else {
            Command.sendMessage("Could not find module with name: " + args[0] + ".");
        }
    }


}
