package com.thnkscj.toolkit.command.commands;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.config.SaveLoad;
import com.thnkscj.toolkit.manager.FriendManager;
import com.thnkscj.toolkit.util.init.Init;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("FakePlayer");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"friend", "f"};
    }

    @Override
    public String getSyntax() {
        return "friend <add | del> <username>";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (command.length() == 0 || command.length() == 1) {
            Command.sendMessage("Lengh has to be more than 2.");
        }

        String aord = args[0];
        String name = args[1];

        switch (aord) {

            case "add":

                if (name == null || name.length() < 2) {
                    Command.sendMessage("You need to type a name.");
                    return;
                }
                if (FriendManager.isFriend(name)) {
                    Command.sendMessage(cf_red + name + cfr + " is already a friend.");
                }

                if (!FriendManager.isFriend(name.toLowerCase()) || !FriendManager.isFriend(name.toUpperCase())) {
                    FriendManager.addFriends(args[1]);
                    Command.sendMessage(cf_green + name + cfr + " has been added.");
                    SaveLoad.saveFriends();
                }
                break;


            case "del":
            case "delete":
            case "remove":

                if (name == null || name.length() < 2) {
                    Command.sendMessage("You need to type a name.");
                    return;
                }
                if (FriendManager.isFriend(name)) {
                    Command.sendMessage(cf_red + name + cfr + " isn't a friend.");
                }
                Init.friendManager.delFriend(name);
                Command.sendMessage(cf_red + name + cfr + " has been removed.");
                break;


        }


    }
}