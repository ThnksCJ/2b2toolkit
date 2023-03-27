package com.thnkscj.toolkit.manager;

import com.thnkscj.toolkit.ToolkitPlayer;
import com.thnkscj.toolkit.util.Wrapper;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class FriendManager extends Wrapper {

    public static Minecraft mc = Minecraft.getMinecraft();

    public static List<ToolkitPlayer> friends;


    public FriendManager() {
        friends = new ArrayList<>();
    }

    public static List<ToolkitPlayer> getFriends() {
        return friends;
    }

    public static boolean isFriend(String name) {
        boolean friend = false;
        for (ToolkitPlayer f : getFriends()) {
            if (f.getName().equalsIgnoreCase(name)) {
                friend = true;
                break;
            }
        }
        return friend;
    }

    public static ToolkitPlayer getFriendByName(String name) {
        ToolkitPlayer fr = null;
        for (ToolkitPlayer f : getFriends()) {
            if (f.getName().equalsIgnoreCase(name)) fr = f;
        }
        return fr;
    }

    public static void addFriends(String name) {
        friends.add(new ToolkitPlayer(name));
    }

    public static void delFriends(String name) {
        friends.remove(getFriendByName(name));
    }

    public static void addFriend(String name) {
        friends.add(new ToolkitPlayer(name));
    }

    public void delFriend(String name) {
        friends.remove(getFriendByName(name));
    }
}

