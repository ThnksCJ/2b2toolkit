package com.thnkscj.toolkit.setting.settings;

import com.thnkscj.toolkit.setting.Setting;

public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(final String name, final String description, final Boolean value) {
        super(name, description, value);
    }

    public void toggle() {
        this.value = !this.value;
    }

    public boolean isEnabled() {
        return this.value;
    }

}
