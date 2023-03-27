package com.thnkscj.toolkit.setting.settings;

import com.thnkscj.toolkit.setting.Setting;

public final class StringSetting extends Setting<String> {

    public StringSetting(final String name, final String description, final String value) {
        super(name, description, value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
