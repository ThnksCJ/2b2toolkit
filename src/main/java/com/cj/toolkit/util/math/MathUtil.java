package com.cj.toolkit.util.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class MathUtil {


    public static float[] calculateAngle(Vec3d from, Vec3d to) {
        double AX = to.x - from.x;
        double AY = (to.y - from.y) * -1.0;
        double AZ = to.z - from.z;
        double dist = MathHelper.sqrt(AX * AX + AZ * AZ);
        return new float[]{(float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(AZ, AX)) - 90.0), (float) MathHelper.wrapDegrees(Math.toDegrees(Math.atan2(AY, dist)))};
    }

    public static Float doubleToFloat(double d) {
        if (d >= Float.MIN_VALUE && d <= Float.MAX_VALUE) {
            return (float) d;
        }
        return null;
    }

    public static float doubleToFloat(double d, float defaultValue) {
        if (d >= Float.MIN_VALUE && d <= Float.MAX_VALUE) {
            return (float) d;
        }
        return defaultValue;
    }

    public static int generateBetween(int minValue, int maxValue) {
        Random r = new Random();

        return r.nextInt(maxValue - minValue) + minValue;
    }

    public static String intToHex(final int value) {
        return "0x" + Integer.toHexString(value);
    }

    public static int hexToInt(String hex) {
        return Integer.valueOf(hex, 16);
    }

    private static long millisToHours(long millis) {
        return millis / 1000 / 60 / 60;
    }
}

