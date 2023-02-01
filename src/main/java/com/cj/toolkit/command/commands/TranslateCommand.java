package com.cj.toolkit.command.commands;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.util.misc.ConverterUtil;
import com.cj.toolkit.util.misc.TranslationUtil;

import java.util.Objects;

public class TranslateCommand extends Command {
    public TranslateCommand() {
        super("Translate");
    }

    public Thread thread;

    @Override
    public String[] getAlias() {
        return new String[]{"translate", "t"};
    }

    @Override
    public String getSyntax() {
        return "translate <source/detect> <target> <message>";
    }

    @Override
    public void onCommand(String command, String[] args) {
        thread = new Thread(() -> {

            if (args[0] == null) {
                Command.sendErrMessage("Source language isn't filled (e.g detect/en)");
                return;
            }
            if (args[0].length() > 2) {
                Command.sendErrMessage("Source language should be 2 characters (e.g detect/en)");
                return;
            }
            if (ConverterUtil.isInt(args[0])) {
                Command.sendErrMessage("Source language should be characters only. (e.g detect/en)");
                return;
            }
            if (args[1] == null) {
                Command.sendErrMessage("Target language isn't filled (e.g pt/pt)");
                return;
            }
            if (args[1].length() > 2) {
                Command.sendErrMessage("Source language should be 2 characters (e.g detect/en)");
                return;
            }
            if (ConverterUtil.isInt(args[1])) {
                Command.sendErrMessage("Source language should be characters only. (e.g detect/en)");
                return;
            }

            if (args[2] == null) {
                Command.sendErrMessage("message isn't filled (e.g hello!)");
                return;
            }
            try {
                if (!Objects.equals(args[0], "detect")) {
                    Command.sendMessage("(" + args[0] + ") " + "->" + " (" + args[1] + ")" + " Your message: " + TranslationUtil.translate(args[0], args[1], args[2] + " " + args[3] + " " + args[4] + " " + args[5]));
                }
            } catch (Exception e) {
                Command.sendErrMessage("Translation error.");
            }
            try {
                if (Objects.equals(args[0], "detect")) {
                    Command.sendMessage("(" + "D" + ") " + "->" + " (" + args[1] + ")" + " Your message: " + TranslationUtil.detectTranslate(args[1], args[2]));
                }
            } catch (Exception e) {
                Command.sendErrMessage("Translation error.");
            }
        });
        thread.start();
    }

}