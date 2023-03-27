package com.thnkscj.toolkit.command.commands;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.config.SaveLoad;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("Reload");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"reload"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        SaveLoad.loadConfig();
        Command.sendMessage("Config reloaded.");
    }
}
