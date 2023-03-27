package com.thnkscj.toolkit.util.misc;

import java.io.InputStream;
import java.util.Scanner;

public class ConverterUtil {

    public static Float doubleToFloat(double doubleValue) {
        return (float) doubleValue;
    }

    public static boolean isInt(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static String convertStreamToString(final InputStream is) {
        final Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "/";
    }
}

