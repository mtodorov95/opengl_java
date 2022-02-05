package entities;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

public class Camera {

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch; // rotation
    private float yaw; // left/right ?
    private float roll; // tilt

    private long window;

    public Camera(long window) {
        this.window = window;
    }

    public void move() {
        GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW.GLFW_KEY_W) {
                position.y += 0.25f;
            }
            if (key == GLFW.GLFW_KEY_D) {
                position.x += 0.25f;
            }
            if (key == GLFW.GLFW_KEY_A) {
                position.x -= 0.25f;
            }
            if (key == GLFW.GLFW_KEY_S) {
                position.y -= 0.25f;
            }
        });
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
