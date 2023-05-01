package com.thnkscj.toolkit.util.shader;

import com.thnkscj.toolkit.util.shader.shaders.BlurShader;

public class Shaders {

    public static void blur(float radius, float dirX, float dirY, Runnable runnable){
        Shader shader = new BlurShader(radius, dirX, dirY);
        shader.draw(runnable);
    }
}
