package com.thnkscj.toolkit.proxy;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.thnkscj.toolkit.util.misc.HttpUtil;
import net.minecraft.client.multiplayer.ServerData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ProxyList {
    private static final Logger LOGGER = LogManager.getLogger();
    private final List<ServerData> servers = Lists.newArrayList();

    public ProxyList() {
        this.loadServerList();
    }

    public void loadServerList() {
        try {
            this.servers.clear();

            Map<String, String> usersMap = new Gson().fromJson(HttpUtil.getResponse("https://pastebin.com/raw/iENeaW2B"), Map.class);

            for (Map.Entry<String, String> entry : usersMap.entrySet()) {
                this.servers.add(new ServerData(entry.getKey(), entry.getValue(), false));
            }
        } catch (Exception exception) {
            LOGGER.error("Couldn't load server list", exception);
        }
    }

    public ServerData getServerData(int index) {
        return this.servers.get(index);
    }

    public int countServers() {
        return this.servers.size();
    }

    public void set(int index, ServerData server) {
        this.servers.set(index, server);
    }
}
