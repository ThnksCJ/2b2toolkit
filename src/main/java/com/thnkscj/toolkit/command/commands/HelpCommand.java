package com.thnkscj.toolkit.command.commands;

import com.google.common.base.Joiner;
import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.command.CommandManager;

import java.util.ArrayList;


public class HelpCommand extends Command {
    public HelpCommand() {
        super("Help");
    }

    @Override
    public String[] getAlias() {
        return new String[]{
                "help", "h"
        };
    }

    @Override
    public String getSyntax() {
        return "help";
    }

    @Override
    public void onCommand(String command, String[] args) {
        ArrayList<String> commandNames = new ArrayList<>();
        CommandManager.getCommands().forEach(c -> commandNames.add(c.getCommand()));

        Command.sendMessage("Commands [" + CommandManager.getCommands().size() + "]: " + cf_green + Joiner.on(", ").join(commandNames));
    }
}


