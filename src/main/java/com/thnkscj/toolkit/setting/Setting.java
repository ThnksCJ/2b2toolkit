package com.thnkscj.toolkit.setting;

public class Setting<T> {

    protected final String name, description;
    protected T value;

    protected Setting(final String name, final String description, final T value) {
        this.name = name;
        this.description = description;
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) {
        this.value = value;
    }

}
