package renderEngine;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class DisplayManager {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static long window;
    private static int width, height;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public static void createDisplay() {
        // Setup an error callback. The default implementation
        // will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        // Configure our window
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
        // version/profile
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GLFW_TRUE);

        // Create the window
        window = glfwCreateWindow(WIDTH, HEIGHT, "Game", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in our rendering loop
        });

        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
                window,
                (vidmode.width() - WIDTH) / 2,
                (vidmode.height() - HEIGHT) / 2
        );
        width = vidmode.width();
        height = vidmode.height();

        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync aka 60 fps
        glfwSwapInterval(1);

        // Make the window visible
        glfwShowWindow(window);
    }

    public static long getWindow() {
        return window;
    }

    public static boolean isKeyPressed(int keycode) {
        return glfwGetKey(window, keycode) == GLFW.GLFW_PRESS;
    }

    public static boolean isCloseRequested() {
        return glfwWindowShouldClose(window);
    }

    public static void updateDisplay() {
        // swap the buffers
        glfwSwapBuffers(window);
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents();
    }

    public static void destroyDisplay() {
        // Terminate GLFW and free the error callback
        cleanUp();
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private static void cleanUp() {
        // Free the window callbacks and destroy the window
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
    }
}