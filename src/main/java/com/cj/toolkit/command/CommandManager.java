package com.cj.toolkit.command;

import com.cj.toolkit.command.commands.*;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.ArrayList;

public class CommandManager {
    public static ArrayList<Command> commands;
    static boolean bo;

    public CommandManager() {
    }

    public static void initCommands() {
        commands = new ArrayList();
        addCommand(new BindCommand());
        addCommand(new DisableCommand());
        addCommand(new EnableCommand());
        addCommand(new FriendCommand());
        addCommand(new HelpCommand());
        addCommand(new PrefixCommand());
        addCommand(new UnbindCommand());
        addCommand(new ReloadCommand());
        addCommand(new SaveCommand());
        addCommand(new RuntimeCommand());
        addCommand(new AntiLeakCommand());
    }

    public static void addCommand(Command cmd) {
        commands.add(cmd);
    }

    public static ArrayList<Command> getCommands() {
        return commands;
    }

    public void callCommand(String input) {
        String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String command = split[0];
        String args = input.substring(command.length()).trim();
        bo = false;
        commands.forEach(c -> {
            for (String s : c.getAlias()) {
                if (s.equalsIgnoreCase(command)) {
                    bo = true;
                    try {
                        c.onCommand(args, args.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                    } catch (Exception e) {
                        Command.sendMessage(ChatFormatting.GRAY + c.getSyntax());
                    }
                }
            }
        });
        if (!bo) {
            Command.sendMessage(ChatFormatting.GRAY + "Command failed.");
        }
    }
}

