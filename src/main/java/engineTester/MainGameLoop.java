package engineTester;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glGetString;

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

        RawModel model = loader.loadToVAO(vertices, indices);

        while (!DisplayManager.isCloseRequested()){
            renderer.prepare();
            shader.start();
            renderer.render(model);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.destroyDisplay();
    }
}