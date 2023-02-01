package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.config.SaveLoad;

public class SaveCommand extends Command {
    public SaveCommand() {
        super("Save");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"save"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        SaveLoad.saveConfig();
        Command.sendMessage("Saved config.");
    }
}
