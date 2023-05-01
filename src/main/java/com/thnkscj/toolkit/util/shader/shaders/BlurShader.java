package com.thnkscj.toolkit.util.shader.shaders;

import com.thnkscj.toolkit.util.shader.Shader;

public class BlurShader extends Shader {

    private final float radius;

    private final float dirX;

    private final float dirY;

    public BlurShader(float radius, float dirX, float dirY) {
        super(DEFAULT_PATH + "blur.glsl");
        this.radius = radius;
        this.dirX = dirX;
        this.dirY = dirY;
    }

    @Override
    public void setup() {
        set1I("sampler", 0);
        set1F("radius", radius);
        set1F("radiusFactor", radius);
        set2F("direction", dirX, dirY);
    }
}
