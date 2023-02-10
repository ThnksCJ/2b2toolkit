package com.cj.toolkit.modules;

import com.cj.toolkit.command.Command;
import com.cj.toolkit.setting.Setting;
import com.cj.toolkit.util.entity.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    public String name, desc;

    private final Category category;

    private boolean enabled;
    private final boolean drawn;

    public int bind;

    protected final List<Setting<?>> settings;


    public Module(String name, String desc, Category category) {

        this.name = name;
        this.desc = desc;
        this.category = category;
        this.bind = Keyboard.KEY_NONE;
        this.enabled = false;
        this.drawn = false;
        setmodule();
        settings = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getBind() {
        return bind;
    }

    public void setBind(int b) {
        bind = b;
    }


    public Category getCategory() {
        return this.category;
    }


    public void setEnabled(boolean e) {
        enabled = e;
    }

    public void enable() {
        setEnabled(true);
        MinecraftForge.EVENT_BUS.register(this);
        if (!getName().equalsIgnoreCase("ClickGUI") && !PlayerUtil.nullcheck()) {
            Command.sendMessage(Command.cf_gray + getName() + " toggled" + Command.cf_green + " on." + Command.cfr);
        }
        onEnable();
    }

    public void disable() {
        setEnabled(false);
        MinecraftForge.EVENT_BUS.unregister(this);
        if (!getName().equalsIgnoreCase("ClickGUI") && !PlayerUtil.nullcheck()) {
            Command.sendMessage(Command.cf_gray + getName() + " toggled" + Command.cf_red + " off." + Command.cfr);
        }
        onDisable();
    }

    protected void onEnable() {
    }

    protected void onDisable() {
    }


    public void toggle() {
        if (isEnabled()) {
            disable();
        } else if (!isEnabled()) {
            enable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void onUpdate() {
    }

    public boolean setmodule() {
        return false;
    }

    protected final void addSettings(final Setting<?>... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }

    public final List<Setting<?>> getSettings() {
        return settings;
    }

    public void onWorldRender() {
    }

}