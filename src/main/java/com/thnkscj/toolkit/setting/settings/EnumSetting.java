package com.thnkscj.toolkit.setting.settings;


import com.thnkscj.toolkit.Toolkit;
import com.thnkscj.toolkit.setting.Setting;

import java.lang.reflect.InvocationTargetException;

public final class EnumSetting<E extends Enum<E>> extends Setting<E> {

    public EnumSetting(final String name, final String description, final E value) {
        super(name, description, value);

    }

    public void cycle() {
        final E[] values = value.getDeclaringClass().getEnumConstants();
        final int newOrdinal = value.ordinal() + 1 >= values.length ? 0 : value.ordinal() + 1;
        this.value = values[newOrdinal];
    }

    public Enum[] getValues() {
        return this.value.getDeclaringClass().getEnumConstants();
    }

    public void setVal(String string) {
        try {
            Object v = this.value.getClass().getMethod("valueOf", String.class).invoke(null, string);
            this.value = (E) v;
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException throwable) {
            Toolkit.log.error("Value: " + string + "doesnt exist", throwable);
        }
    }

    public String getValueName() {
        return value.name();
    }


}

