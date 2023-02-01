package com.cj.toolkit.util.entity;

import static com.cj.toolkit.util.Wrapper.mc;


public class PlayerUtil {
    public static boolean nullcheck() {
        return mc.player == null || mc.world == null;
    }
}
