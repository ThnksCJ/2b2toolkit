package com.thnkscj.toolkit.util.misc;

public class TimerUtil {
    private long current;

    public TimerUtil() {
        this.current = System.currentTimeMillis();
    }

    public boolean passed(final long delay) {
        return System.currentTimeMillis() - this.current >= delay;
    }

    public long elapsed() {
        return time() - this.current;
    }

    public void reset() {
        this.current = System.currentTimeMillis();
    }

    public long time() {
        return System.currentTimeMillis();
    }

    public long getTime() {
        return current;
    }
}
