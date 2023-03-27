package com.thnkscj.toolkit.util.entity;

import static com.thnkscj.toolkit.util.Wrapper.mc;


public class PlayerUtil {
    public static boolean nullcheck() {
        return mc.player == null || mc.world == null;
    }
}
