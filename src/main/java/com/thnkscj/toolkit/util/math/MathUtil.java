package com.thnkscj.toolkit.util.math;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
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

    public static int getRandomInRange(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static float getRandomInRange(float min, float max) {
        SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }

    public static double getRandomInRange(double min, double max) {
        SecureRandom random = new SecureRandom();
        return random.nextDouble() * (max - min) + min;
    }

    public static double lerp(double old, double newVal, double amount) {
        return (1.0 - amount) * old + amount * newVal;
    }

    public static Double interpolate(double oldValue, double newValue, double interpolationValue){
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).floatValue();
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue){
        return interpolate(oldValue, newValue, (float) interpolationValue).intValue();
    }

    public static float calculateGaussianValue(float x, float sigma) {
        double PI = 3.141592653;
        double output = 1.0 / Math.sqrt(2.0 * PI * (sigma * sigma));
        return (float) (output * Math.exp(-(x * x) / (2.0 * (sigma * sigma))));
    }

    public static double roundToHalf(double d) {
        return Math.round(d * 2) / 2.0;
    }

    public static double round(double num, double increment) {
        BigDecimal bd = new BigDecimal(num);
        bd = (bd.setScale((int) increment, RoundingMode.HALF_UP));
        return bd.doubleValue();
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static float getRandomFloat(float max, float min) {
        SecureRandom random = new SecureRandom();
        return random.nextFloat() * (max - min) + min;
    }

}
