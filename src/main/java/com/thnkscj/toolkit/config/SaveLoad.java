package com.thnkscj.toolkit.config;

import com.google.gson.*;
import com.thnkscj.toolkit.Toolkit;
import com.thnkscj.toolkit.ToolkitPlayer;
import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.manager.FriendManager;
import com.thnkscj.toolkit.modules.Module;
import com.thnkscj.toolkit.modules.ModuleManager;
import com.thnkscj.toolkit.setting.Setting;
import com.thnkscj.toolkit.setting.settings.*;
import com.thnkscj.toolkit.util.misc.ColorUtil;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveLoad {

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    public static Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    public static String s = DATE_FORMAT.format(new Date());

    public static void saveConfig() {
        try {
            saveModules();
            saveFriends();
            savePrefix();
        } catch (IOException ignored) {
            Toolkit.log.info("Error saving modules.");
        }
    }

    public static void loadConfig() {
        try {
            createDirectory();
            loadModules();
            loadFriends();
            loadPrefix();
        } catch (IOException ignored) {

        }
    }

    public static void createDirectory() throws IOException {
        if (!Files.exists(Paths.get("2b2toolkit/")))
            Files.createDirectories(Paths.get("2b2toolkit/"));

        if (!Files.exists(Paths.get("2b2toolkit/modules/")))
            Files.createDirectories(Paths.get("2b2toolkit/modules/"));

        if (!Files.exists(Paths.get("2b2toolkit/modules/client")))
            Files.createDirectories(Paths.get("2b2toolkit/modules/client"));

        if (!Files.exists(Paths.get("2b2toolkit/prefix.txt")))
            Files.createFile(Paths.get("2b2toolkit/prefix.txt"));
    }

    public static void registerFiles(String name, String path) throws IOException {
        if (!Files.exists(Paths.get("2b2toolkit/" + path + "/" + name + ".json"))) {
            Files.createFile(Paths.get("2b2toolkit/" + path + "/" + name + ".json"));

        } else {
            File file = new File("2b2toolkit/" + path + "/" + name + ".json");
            file.delete();
            Files.createFile(Paths.get("2b2toolkit/" + path + "/" + name + ".json"));
        }
    }

    public static void saveModules() throws IOException {
        for (Module module : ModuleManager.getModules()) {
            registerFiles(module.getName(), "modules/" + module.getCategory().toString().toLowerCase());
            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("2b2toolkit/modules/" + module.getCategory().toString().toLowerCase() + "/" + module.getName() + ".json"), StandardCharsets.UTF_8);

            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();

            moduleObject.add("Name", new JsonPrimitive(module.getName()));
            moduleObject.add("Enabled", new JsonPrimitive(module.isEnabled()));
            moduleObject.add("Keybind", new JsonPrimitive(Keyboard.getKeyName(module.getBind())));

            for (Setting setting : module.getSettings()) {
                if (setting instanceof BooleanSetting)
                    settingObject.add(setting.getName(), new JsonPrimitive(((BooleanSetting) setting).getValue()));

                if (setting instanceof DoubleSetting)
                    settingObject.add(setting.getName(), new JsonPrimitive(((DoubleSetting) setting).getValue()));

                if (setting instanceof IntegerSetting)
                    settingObject.add(setting.getName(), new JsonPrimitive(((IntegerSetting) setting).getValue()));

                if (setting instanceof EnumSetting)
                    settingObject.add(setting.getName(), new JsonPrimitive(((EnumSetting<?>) setting).getValueName()));

                if (setting instanceof ColorSetting) {
                    settingObject.add(setting.getName(), new JsonPrimitive(ColorUtil.toHex(((ColorSetting) setting).getColor())));
                    settingObject.add(setting.getName() + " Rainbow", new JsonPrimitive(((ColorSetting) setting).rainbow));
                }


                if (setting instanceof StringSetting)
                    settingObject.add(setting.getName(), new JsonPrimitive(((StringSetting) setting).getValue()));
            }

            moduleObject.add("Settings", settingObject);
            String jsonString = gson.toJson(new JsonParser().parse(moduleObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }
    }

    public static void loadModules() throws IOException {
        for (Module module : ModuleManager.getModules()) {
            if (!Files.exists(Paths.get("2b2toolkit/modules/" + module.getCategory().toString().toLowerCase() + "/" + module.getName() + ".json")))
                continue;

            JsonObject moduleObject = null;

            try {
                InputStream inputStream = Files.newInputStream(Paths.get("2b2toolkit/modules/" + module.getCategory().toString().toLowerCase() + "/" + module.getName() + ".json"));
                moduleObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();
            } catch (Exception ignored) {
            }

            assert moduleObject != null;
            if (moduleObject.get("Name") == null)
                continue;

            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();

            for (Setting setting : module.getSettings()) {
                JsonElement settingValueObject = null;
                JsonElement rainbowSettingValueObject = null;

                if (setting instanceof BooleanSetting) {
                    settingValueObject = settingObject.get(setting.getName());
                }

                if (setting instanceof DoubleSetting) {
                    settingValueObject = settingObject.get(setting.getName());
                }

                if (setting instanceof IntegerSetting) {
                    settingValueObject = settingObject.get(setting.getName());
                }

                if (setting instanceof EnumSetting) {
                    settingValueObject = settingObject.get(setting.getName());
                }

                if (setting instanceof ColorSetting) {
                    settingValueObject = settingObject.get(setting.getName());
                    rainbowSettingValueObject = settingObject.get(setting.getName() + " Rainbow");
                }

                if (setting instanceof StringSetting) {
                    settingValueObject = settingObject.get(setting.getName());
                }

                if (settingValueObject != null) {
                    if (setting instanceof BooleanSetting) {
                        setting.setValue(settingValueObject.getAsBoolean());
                    }

                    if (setting instanceof DoubleSetting) {
                        setting.setValue(settingValueObject.getAsDouble());
                    }

                    if (setting instanceof IntegerSetting) {
                        setting.setValue(settingValueObject.getAsInt());
                    }

                    if (setting instanceof ColorSetting) {
                        setting.setValue(ColorUtil.hexToColor(settingValueObject.getAsString()));
                        ((ColorSetting) setting).setRainbow(rainbowSettingValueObject.getAsBoolean());
                    }

                    if (setting instanceof EnumSetting) {
                        ((EnumSetting<?>) setting).setVal(settingValueObject.getAsString());
                    }

                    if (setting instanceof StringSetting) {
                        setting.setValue(settingValueObject.getAsString());
                    }
                }
            }
            module.setBind(Keyboard.getKeyIndex(moduleObject.get("Keybind").getAsString()));
            if (moduleObject.get("Enabled").getAsBoolean()) module.enable();
        }
    }

    public static void saveFriends() throws IOException {
        registerFiles("friends", "");

        OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(new FileOutputStream("2b2toolkit/friends.json"), StandardCharsets.UTF_8);
        JsonObject mainObject = new JsonObject();
        JsonArray friendArray = new JsonArray();

        for (ToolkitPlayer toolkitPlayer : FriendManager.getFriends()) {
            friendArray.add(toolkitPlayer.getName());
        }

        mainObject.add("Friends", friendArray);
        String jsonString = gson.toJson(new JsonParser().parse(mainObject.toString()));
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadFriends() throws IOException {
        if (!Files.exists(Paths.get("2b2toolkit/friends.json")))
            return;

        InputStream inputStream = Files.newInputStream(Paths.get("2b2toolkit/friends.json"));
        JsonObject mainObject = new JsonParser().parse(new InputStreamReader(inputStream)).getAsJsonObject();

        if (mainObject.get("Friends") == null)
            return;
        JsonArray friendObject = mainObject.get("Friends").getAsJsonArray();

        friendObject.forEach(object -> FriendManager.addFriend(object.getAsString()));

        inputStream.close();
    }

    public static void savePrefix() throws IOException {
        FileWriter writer = new FileWriter("2b2toolkit/prefix.txt");
        BufferedWriter buff = new BufferedWriter(writer);

        buff.write(Command.prefix);
        buff.close();
    }

    public static void loadPrefix() throws IOException {
        if (Files.exists(Paths.get("2b2toolkit/prefix.txt"))) {
            FileReader fileReader = new FileReader("2b2toolkit/prefix.txt");

            BufferedReader buffReader = new BufferedReader(fileReader);

            String string = buffReader.readLine();

            if (string.equals(""))
                return;

            Command.setPrefix(string);

            buffReader.close();
        }
    }
}