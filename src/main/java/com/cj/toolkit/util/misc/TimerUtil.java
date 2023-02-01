package com.cj.toolkit.util.misc;

public class TimerUtil {
    private long time;

    public TimerUtil() {
        this.time = System.currentTimeMillis();
    }

    public boolean passed(double delay) {
        return System.currentTimeMillis() - this.time >= delay;
    }

    public void reset() {
        this.time = System.currentTimeMillis();
    }

    public long getPassedTime() {
        return System.currentTimeMillis() - this.time;
    }

    public long time() {
        return System.currentTimeMillis();
    }

    public void set(long time) {
        this.time = time;
    }

}
