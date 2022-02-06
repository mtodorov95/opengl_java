package entities;

import models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import renderEngine.DisplayManager;

public class Player extends Entity {

    private static final float RUN_SPEED = 20;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -50;
    private static final float JUMP_POWER = 30;
    private static final float TERRAIN_HEIGHT = 0;
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardsSpeed = 0;
    private boolean isInAir = false;
    private long window;

    public Player(TexturedModel model, Vector3f position, float rotX, float rotY, float rotZ, float scale, long window) {
        super(model, position, rotX, rotY, rotZ, scale);
        this.window = window;
    }

    public void move(float delta) {
        checkInputs();
        super.increaseRotation(0, currentTurnSpeed * delta, 0);
        float distance = currentSpeed * delta;
        float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
        float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
        super.increasePosition(dx, 0, dz);
        upwardsSpeed += GRAVITY * delta;
        super.increasePosition(0, upwardsSpeed * delta, 0);
        if (super.getPosition().y < TERRAIN_HEIGHT) {
            upwardsSpeed = 0;
            isInAir = false;
            Vector3f position = super.getPosition();
            position.y = TERRAIN_HEIGHT;
            super.setPosition(position);
        }
    }

    private void jump() {
        if (!isInAir) {
            this.upwardsSpeed = JUMP_POWER;
            isInAir = true;
        }
    }

    private void checkInputs() {
        if (DisplayManager.isKeyPressed(GLFW.GLFW_KEY_W)) {
            this.currentSpeed = RUN_SPEED;
        } else if (DisplayManager.isKeyPressed(GLFW.GLFW_KEY_S)) {
            this.currentSpeed = -RUN_SPEED;
        } else {
            this.currentSpeed = 0;
        }

        if (DisplayManager.isKeyPressed(GLFW.GLFW_KEY_D)) {
            this.currentTurnSpeed = -TURN_SPEED;
        } else if (DisplayManager.isKeyPressed(GLFW.GLFW_KEY_A)) {
            this.currentTurnSpeed = TURN_SPEED;
        } else {
            this.currentTurnSpeed = 0;
        }

        if (DisplayManager.isKeyPressed(GLFW.GLFW_KEY_SPACE)) {
            jump();
        }
    }
}
