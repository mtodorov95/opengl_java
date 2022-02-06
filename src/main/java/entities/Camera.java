package entities;

import org.joml.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0, 10, 0);
    private float pitch; // rotation
    private float yaw; // left/right ?
    private float roll; // tilt

    private long window;

    public Camera(long window) {
        this.window = window;
    }

    public void move() {

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
