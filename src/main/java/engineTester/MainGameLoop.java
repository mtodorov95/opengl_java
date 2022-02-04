package engineTester;

import models.TexturedModel;
import org.lwjgl.opengl.GL;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.TextureModel;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

        // OpenGL expects vertices to be defined counter clockwise

        // Rect
        float[] vertices = {
                -0.5f, 0.5f, 0f, //v0
                -0.5f, -0.5f, 0f, //v1
                0.5f, -0.5f, 0f, //v2
                0.5f, 0.5f, 0f, //v3
        };

        int[] indices = {
                0,1,3, // top left tri (v0,v1,v3)
                3,1,2 // bottom right tri (v3,v1,v2)
        };

        // u, v coordinates. From TL(0,0) to BR(1,1)
        float[] textureCoords = {
                0,0, //v0
                0,1, //v1
                1,1, //v2
                1,0, //v3
        };

        RawModel model = loader.loadToVAO(vertices,textureCoords,indices);
        TextureModel texture = new TextureModel(loader.loadTexture("src/main/resources/res/tile.png"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        while (!DisplayManager.isCloseRequested()){
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.destroyDisplay();
    }
}