package skybox;

import entities.Camera;
import org.joml.Matrix4f;
import shaders.ShaderProgram;
import tools.Maths;

public class SkyBoxShader extends ShaderProgram {


    private static final String VERTEX_FILE = "src/main/resources/shaders/skyVertex.vs";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/skyFragment.fs";

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public SkyBoxShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(location_projectionMatrix, matrix);
    }

    // Apply only the rotation of the matrix by setting translation to 0.
    public void loadViewMatrix(Camera camera) {
        Matrix4f matrix = Maths.createViewMatrix(camera);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);
        super.loadMatrixToUniform(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
