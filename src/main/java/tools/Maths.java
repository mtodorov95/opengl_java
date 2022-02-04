package tools;

import entities.Camera;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().translate(translation).
                rotateX((float) Math.toRadians(rx)).
                rotateY((float) Math.toRadians(ry)).
                rotateZ((float) Math.toRadians(rz)).
                scale(scale);
        return matrix;
    }

    // View matrix simulates the presence of a camera by moving all objects in reverse of the Camera instance.
    public static Matrix4f createViewMatrix(Camera camera){
        Matrix4f matrix = new Matrix4f();
        matrix.identity().
                rotateX((float) Math.toRadians(camera.getPitch())).
                rotateY((float) Math.toRadians(camera.getYaw()));
        Vector3f position = camera.getPosition();
        Vector3f negativePosition = new Vector3f(-position.x, -position.y, -position.z);
        matrix.translate(negativePosition);
        return matrix;
    }
}
