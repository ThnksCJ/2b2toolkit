package com.thnkscj.toolkit.command.commands;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.config.SaveLoad;

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
