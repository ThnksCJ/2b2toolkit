package com.thnkscj.toolkit.modules;

import com.thnkscj.toolkit.modules.modules.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ModuleManager {

    public static ArrayList<Module> mods;
    public static Module module;

    public ModuleManager() {
        mods = new ArrayList<>();

        addModule(new ClickGui());
        addModule(new AntiLeak());
        addModule(new TNTHelper());
        addModule(new AntiElytraCrash());
        addModule(new AutoQueueSkip());
        addModule(new AutoLogTimer());
        addModule(new Notifications());
        addModule(new PortalInfo());
        addModule(new GameplayTweaks());
        addModule(new FakePlayer());
        addModule(new Clans());
        addModule(new ElytraFly());
    }

    public static Module getModule(String name) {

        for (Module md : mods) {
            if (md.getName().equalsIgnoreCase(name)) {
                return md;
            }
        }
        return null;
    }

    public static ArrayList<Module> getModuleByCategory(Category category) {
        ArrayList<Module> modules = new ArrayList<Module>();

        for (Module md : mods) {
            if (md.getCategory() == category) {
                modules.add(md);
            }
        }
        return modules;
    }

    public static ArrayList<Module> getModules() {
        return mods;
    }

    public static Module getModuleName(String name) {
        return getModules().stream().filter(mm -> mm.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static <T extends Module> T getModule(final Class<T> clazz) throws NoSuchElementException {
        for (final Module module : mods)
            if (module.getClass().isAssignableFrom(clazz)) return (T) module;

        throw new NoSuchElementException();
    }

    public static void addModule(Module mod) {
        mods.add(mod);
    }

    public static void onUpdate() {
        mods.stream().filter(Module::isEnabled).forEach(Module::onUpdate);
    }

    public static void onBind(int key) {
        if (key == 0) return;
        mods.forEach(module -> {
            if (module.getBind() == key) {
                if (!module.isEnabled()) {
                    module.enable();
                } else {
                    module.disable();
                }
            }
        });
    }

    public ArrayList<Module> getModuleList() {
        return mods;
    }
}