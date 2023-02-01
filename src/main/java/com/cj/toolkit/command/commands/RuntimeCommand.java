package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;

import static com.cj.toolkit.util.init.Init.startTime;

public class RuntimeCommand extends Command {
    public RuntimeCommand() {
        super("runtime");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"runtime"};
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public void onCommand(String command, String[] args) {
        final long duration = System.nanoTime() - startTime;

        Command.sendMessage("You have played for: " + duration / 1000000000 + " seconds.");
    }
}
