package com.cj.toolkit.mixin;

import java.lang.annotation.Native;

public class Precedence {

    @Native
    public static final int MAXIMUM = 0x7fffffff;

    @Native
    public static final int URGENT = 10000;

    @Native
    public static final int HIGH = 5000;

    @Native
    public static final int NORMAL = 1001;

    @Native
    public static final int LOW = 800;


}
