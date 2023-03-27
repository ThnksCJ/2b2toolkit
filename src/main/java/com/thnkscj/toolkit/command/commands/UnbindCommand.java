package com.thnkscj.toolkit.command.commands;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.modules.ModuleManager;
import org.lwjgl.input.Keyboard;

public class UnbindCommand extends Command {
    public UnbindCommand() {
        super("Unbind");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"unbind"};
    }

    @Override
    public String getSyntax() {
        return "unbind <module>";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        for (Module module : ModuleManager.getModules()) {
            if (module.getName().equalsIgnoreCase(args[0])) {
                module.setBind(Keyboard.KEY_NONE);
                Command.sendMessage(module.getName() + "unbound.");

            }
        }
    }
}