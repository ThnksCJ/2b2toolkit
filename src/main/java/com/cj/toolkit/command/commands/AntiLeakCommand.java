package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.modules.modules.AntiLeak;
import net.minecraft.util.math.BlockPos;

public class AntiLeakCommand extends Command {
    public AntiLeakCommand() {
        super("AntiLeak");
    }

    @Override
    public String[] getAlias() {
        return new String[]{"antileak", "al"};
    }

    @Override
    public String getSyntax() {
        return "antileak offset <x> <y> <z>";
    }

    @Override
    public void onCommand(String command, String[] args) throws Exception {
        if (args.length == 0) {
            sendMessage("Usage: " + getSyntax());
            return;
        }
        if (args[0].equalsIgnoreCase("offset")) {
            if (args.length != 4) {
                sendMessage("Usage: " + getSyntax());
                return;
            }
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);
            AntiLeak.offsetRand = new BlockPos(x, y, z);
            sendMessage("Offset set to " + x + ", " + y + ", " + z);
        }
    }
}
