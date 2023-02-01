package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;

public class PrefixCommand extends Command {
    public PrefixCommand() {
        super("Prefix");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"prefix", "p"};
    }

    @Override
    public String getSyntax() {
        return "prefix <prefix>";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("/") || args[0].equalsIgnoreCase("#")) {
            Command.sendMessage("failed.");
        } else {
            Command.setPrefix(args[1]);
            Command.sendMessage("Prefix set: " + Command.cf_aqua + args[1]);
        }
    }
}
