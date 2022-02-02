package engineTester;

import org.lwjgl.opengl.GL;
import renderEngine.DisplayManager;

import static org.lwjgl.opengl.GL11.glClearColor;


public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        while (!DisplayManager.isCloseRequested()){
            DisplayManager.updateDisplay();
        }

        DisplayManager.destroyDisplay();
    }
}