package com.cj.toolkit.setting.settings;

import com.cj.toolkit.setting.Setting;

public final class IntegerSetting extends Setting<Integer> {
    private final Integer min, max;

    public IntegerSetting(final String name, final String description, final Integer min, final Integer value, final Integer max) {
        super(name, description, value);

        this.min = min;
        this.max = max;
    }


    public double getMaximumValue() {
        return this.max;
    }


    public double getMinimumValue() {
        return this.min;
    }

}
