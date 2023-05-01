package com.thnkscj.toolkit.modules;

import com.thnkscj.toolkit.command.Command;
import com.thnkscj.toolkit.event.events.render.Render2DEvent;
import com.thnkscj.toolkit.modules.modules.client.Notifications;
import com.thnkscj.toolkit.setting.Setting;
import com.thnkscj.toolkit.util.entity.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Module {

    protected static final Minecraft mc = Minecraft.getMinecraft();
    protected final List<Setting<?>> settings;
    private final Category category;
    private final boolean drawn;
    public String name, desc;
    public int bind;
    private boolean enabled;


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

    public void enable() {
        setEnabled(true);
        MinecraftForge.EVENT_BUS.register(this);
        if (!getName().equalsIgnoreCase("ClickGUI") && !getName().equalsIgnoreCase("HudEditor") && !PlayerUtil.nullcheck() && Notifications.chat.getValue()) {
            Command.sendMessage(Command.cf_gray + getName() + " toggled" + Command.cf_green + " on." + Command.cfr);
        }
        onEnable();
    }

    public void disable() {
        setEnabled(false);
        MinecraftForge.EVENT_BUS.unregister(this);
        if (!getName().equalsIgnoreCase("ClickGUI") && !getName().equalsIgnoreCase("HudEditor") && !PlayerUtil.nullcheck() && Notifications.chat.getValue()) {
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
        } else {
            enable();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean e) {
        enabled = e;
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

    public void onWorldRender(RenderWorldLastEvent event) {
    }

    public void onRender2D(Render2DEvent event) {
    }
}