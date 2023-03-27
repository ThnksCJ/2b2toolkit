package com.thnkscj.toolkit.command.commands;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.util.init.Init;

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
        final long duration = System.nanoTime() - Init.startTime;

        Command.sendMessage("You have played for: " + duration / 1000000000 + " seconds.");
    }
}
