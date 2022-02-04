package shaders;

import entities.Camera;
import org.joml.Matrix4f;
import tools.Maths;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/main/resources/shaders/vertex.vs";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/fragment.fs";
    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrixToUniform(transformationMatrixLocation, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrixToUniform(projectionMatrixLocation, matrix);
    }

    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrixToUniform(viewMatrixLocation, viewMatrix);
    }
}
