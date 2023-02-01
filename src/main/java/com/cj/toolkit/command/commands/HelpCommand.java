package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.command.CommandManager;
import com.google.common.base.Joiner;


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
        Command.sendMessage("Commands [" + CommandManager.getCommands().size() + "]: " + cf_green + Joiner.on(", ").join(CommandManager.commands.toArray()));
    }
}


