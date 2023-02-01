package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.modules.ModuleManager;
import com.cj.toolkit.modules.Module;
import org.lwjgl.input.Keyboard;

public class BindCommand extends Command {
    public BindCommand() {
        super("Bind");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"bind", "b"};
    }

    @Override
    public String getSyntax() {
        return "bind <module> <bind>";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        for (Module module : ModuleManager.getModules()) {
            if (module.getName().equalsIgnoreCase(args[0])) {
                if (args[1].length() == 1) {
                    module.setBind(Keyboard.getKeyIndex(args[1].toUpperCase()));
                    Command.sendMessage(module.getName() + "bound to " + args[1] + ".");
                } else {
                    Command.sendErrMessage(getSyntax());
                }
            }
        }
    }
}
