package com.thnkscj.toolkit.modules;

public enum Category {
    CLIENT("Client"),
    HUD("HUD");

    public final String ModuleName;

    Category(String ModuleName) {
        this.ModuleName = ModuleName;
    }
}

