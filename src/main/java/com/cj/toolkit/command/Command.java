package com.cj.toolkit.command;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

public abstract class Command {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static String prefix = ".";

    public String command;

    public static ChatFormatting cf_gray = ChatFormatting.GRAY;
    public static ChatFormatting cf_aqua = ChatFormatting.AQUA;
    public static ChatFormatting cf_yellow = ChatFormatting.YELLOW;
    public static ChatFormatting cf_red = ChatFormatting.RED;
    public static ChatFormatting cf_green = ChatFormatting.GREEN;
    public static ChatFormatting cfr = ChatFormatting.RESET;


    public static void sendMessage(String message) {
        if (mc.player != null || mc.world != null) {
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[" + ChatFormatting.LIGHT_PURPLE + "2b2toolkit" + cfr + "] " + message), 69);
        }
    }

    public static void sendErrMessage(String message) {
        Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(new TextComponentString("[" + ChatFormatting.LIGHT_PURPLE + "2b2toolkit" + cfr + "] " + cf_red + " Error: " + cfr + message), 69);
    }

    public static String getPrefix() {
        return prefix;
    }

    public static void setPrefix(String newPrefix) {
        prefix = newPrefix;
    }

    public abstract String[] getAlias();

    public abstract String getSyntax();

    public abstract void onCommand(String command, String[] args) throws Exception;

    public String getCommand() {
        return this.command;
    }

    public Command(final String command) {
        this.command = command;
    }
}

