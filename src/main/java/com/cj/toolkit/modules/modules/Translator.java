package com.cj.toolkit.modules.modules;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.modules.Category;
import com.cj.toolkit.modules.Module;
import com.cj.toolkit.setting.settings.BooleanSetting;
import com.cj.toolkit.setting.settings.EnumSetting;
import com.cj.toolkit.util.misc.TranslationUtil;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

import static com.cj.toolkit.command.Command.cfr;

public class Translator extends Module {
    public Translator() {
        super("Translator", "Automatically translates chat/client", Category.CLIENT);


        addSettings(chat, modules, commands, mode, target, source, type);
    }

    public enum typeofchat {Client, Server}

    public enum chatmodes {Detect, Target}

    public enum targetlanguage {Af, De, En, Es, Fi, Fr, He, Hi, It, Ja, Nl, Pl, Pt, Ro, Ru, Sb, Si, Th, Tr, Ua, Ur, Vi, Zu}

    public enum sourcelanguage {Af, De, En, Es, Fi, Fr, He, Hi, It, Ja, Nl, Pl, Pt, Ro, Ru, Sb, Si, Th, Tr, Ua, Ur, Vi, Zu}

    public BooleanSetting chat = new BooleanSetting("Chat", "", true);
    public BooleanSetting modules = new BooleanSetting("Modules", "", true);
    public BooleanSetting commands = new BooleanSetting("Commands", "", false);
    public EnumSetting<chatmodes> mode = new EnumSetting<>("ChatMode", "", chatmodes.Detect);
    public static EnumSetting<targetlanguage> target = new EnumSetting<>("TargetLanguage", "", targetlanguage.Pt);
    public static EnumSetting<sourcelanguage> source = new EnumSetting<>("SourceLanguage", "", sourcelanguage.En);
    public EnumSetting<typeofchat> type = new EnumSetting<>("TypeOfChat", "", typeofchat.Client);

    @Override
    public void onEnable() {
        Command.sendMessage("Translations aren't 100% accurate.");
    }

    @SubscribeEvent
    public void onClientChatReceived(ClientChatReceivedEvent event) {
        Thread thread = new Thread(() -> {
            Iterable<NetworkPlayerInfo> players = Objects.requireNonNull(mc.getConnection()).getPlayerInfoMap();
            while (players.iterator().hasNext()) {
                String message = event.getMessage().getFormattedText().replace("<", Command.cf_aqua.toString() + "{").replace(">", Command.cf_aqua + "}" + cfr.toString());
                String cmg = event.getMessage().getFormattedText().replaceAll("<br>", "").replaceAll("\u00a7", "");
                if (type.getValue() == typeofchat.Client) {
                    try {
                        Command.sendMessage(TranslationUtil.translate(source.getValue().toString(), target.getValue().toString(), message));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        mc.player.connection.sendPacket(new CPacketChatMessage(TranslationUtil.translate(source.getValue().toString(), target.getValue().toString(), cmg)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        });
        thread.start();
    }
}

