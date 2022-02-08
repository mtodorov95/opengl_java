package guis;

import org.joml.Matrix4f;
import shaders.ShaderProgram;

public class GUIShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/main/resources/shaders/guiVertex.vs";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/guiFragment.fs";

    private int location_transformationMatrix;

    public GUIShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    public void loadTransformation(Matrix4f matrix) {
        super.loadMatrixToUniform(location_transformationMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
