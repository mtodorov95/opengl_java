package shaders;

import entities.Camera;
import entities.Light;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import tools.Maths;

public class StaticShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/main/resources/shaders/vertex.vs";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/fragment.fs";
    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int useFakeLightingLocation;
    private int skyColorLocation;
    private int offsetLocation;
    private int numberOfRowsLocation;

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
        lightPositionLocation = super.getUniformLocation("lightPosition");
        lightColorLocation = super.getUniformLocation("lightColor");
        shineDamperLocation = super.getUniformLocation("shineDamper");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        useFakeLightingLocation = super.getUniformLocation("useFakeLighting");
        skyColorLocation = super.getUniformLocation("skyColor");
        offsetLocation = super.getUniformLocation("offset");
        numberOfRowsLocation = super.getUniformLocation("numberOfRows");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void loadSkyColor(Vector3f rgb) {
        super.loadVectorToUniform(skyColorLocation, rgb);
    }

    public void loadFakeLightingVar(boolean useFake) {
        super.loadBooleanToUniform(useFakeLightingLocation, useFake);
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloatToUniform(numberOfRowsLocation, numberOfRows);
    }

    public void loadTextureOffset(float x, float y) {
        super.loadVectorToUniform(offsetLocation, new Vector2f(x, y));
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloatToUniform(shineDamperLocation, damper);
        super.loadFloatToUniform(reflectivityLocation, reflectivity);
    }

    public void loadLight(Light light) {
        super.loadVectorToUniform(lightPositionLocation, light.getPosition());
        super.loadVectorToUniform(lightColorLocation, light.getColor());
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(transformationMatrixLocation, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrixToUniform(projectionMatrixLocation, matrix);
    }

    public void loadViewMatrix(Camera camera) {
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrixToUniform(viewMatrixLocation, viewMatrix);
    }
}
