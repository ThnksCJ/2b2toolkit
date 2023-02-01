package com.cj.toolkit.setting.settings;

import com.cj.toolkit.setting.Setting;

public final class DoubleSetting extends Setting<Double> {
    private final Double min, max;

    public DoubleSetting(final String name, final String description, final Double min, final Double value, final Double max) {
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
