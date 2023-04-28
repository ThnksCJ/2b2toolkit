package com.thnkscj.toolkit.util.world;

import static com.thnkscj.toolkit.util.Wrapper.mc;

public class TimerUtil {
    public static void setTimer(final float speed) {
        mc.timer.tickLength = 50f / speed;
    }

    public static float getTimer() {
        return mc.timer.tickLength;
    }

    public static void resetTimer() {
        mc.timer.tickLength = 50f;
    }
}
