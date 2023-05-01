package com.thnkscj.toolkit.util.shader;

import com.thnkscj.toolkit.Toolkit;
import net.minecraft.client.Minecraft;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public abstract class Shader {

    public static String DEFAULT_PATH = ""; //"assets.minecraft/shaders/program/";

    private int shaderID;

    public Shader(String shaderPath){
        createShaderFromPath(shaderPath);
    }


    public abstract void setup();


    public void draw(Runnable runnable){
        attach();
        setup();
        runnable.run();
        detach();
    }


    public void attach(){
        GL11.glPushMatrix();
        ARBShaderObjects.glUseProgramObjectARB(shaderID);
    }

    public void detach(){
        ARBShaderObjects.glUseProgramObjectARB(0);
        GL11.glPopMatrix();
    }

    public void set1I(String name, int value0) {
        ARBShaderObjects.glUniform1iARB(ARBShaderObjects.glGetUniformLocationARB(shaderID, name), value0);
    }

    public void set1F(String name, float value0) {
        ARBShaderObjects.glUniform1fARB(ARBShaderObjects.glGetUniformLocationARB(shaderID, name), value0);
    }

    public void set2F(String name, float value0, float value1) {
        ARBShaderObjects.glUniform2fARB(ARBShaderObjects.glGetUniformLocationARB(shaderID, name), value0, value1);
    }

    public void set3F(String name, float value0, float value1, float value2) {
        ARBShaderObjects.glUniform3fARB(ARBShaderObjects.glGetUniformLocationARB(shaderID, name), value0, value1, value2);
    }

    public void set4F(String name, float value0, float value1, float value2, float value3) {
        ARBShaderObjects.glUniform4fARB(ARBShaderObjects.glGetUniformLocationARB(shaderID, name), value0, value1, value2, value3);
    }


    private void createShaderFromPath(String path){
        InputStream stream = getClass().getClassLoader().getResourceAsStream(path);
        if(stream == null){
            Toolkit.log.error("Could not create shader: " + path);
            return;
        }
        String code;
        try {
            code = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch(IOException e){
            Toolkit.log.error(e);
            return;
        }
        createShader(code);
    }

    private void createShader(String str){
        int shaderProgram = ARBShaderObjects.glCreateProgramObjectARB();
        if (shaderProgram == 0) {
            System.out.println("PC Issued");
            Minecraft.getMinecraft().shutdown();
            return;
        }
        int shader = ARBShaderObjects.glCreateShaderObjectARB(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        ARBShaderObjects.glShaderSourceARB(shader, str);
        ARBShaderObjects.glCompileShaderARB(shader);
        ARBShaderObjects.glAttachObjectARB(shaderProgram, shader);
        ARBShaderObjects.glLinkProgramARB(shaderProgram);
        this.shaderID = shaderProgram;
    }
}
