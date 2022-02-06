package shaders;

import entities.Camera;
import entities.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import tools.Maths;

public class TerrainShader extends ShaderProgram {

    private static final String VERTEX_FILE = "src/main/resources/shaders/terrainVertex.vs";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/terrainFragment.fs";
    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int skyColorLocation;
    private int grassTerrainLocation;
    private int mudTerrainLocation;
    private int flowerTerrainLocation;
    private int pathTerrainLocation;
    private int blendMapLocation;

    public TerrainShader() {
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
        skyColorLocation = super.getUniformLocation("skyColor");

        grassTerrainLocation = super.getUniformLocation("terrainTexture");
        mudTerrainLocation = super.getUniformLocation("terrainTexture2");
        flowerTerrainLocation = super.getUniformLocation("terrainTexture3");
        pathTerrainLocation = super.getUniformLocation("terrainTexture4");
        blendMapLocation = super.getUniformLocation("blendMap");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    public void connectTextureUnits() {
        // texture          //GL13.GL_TEXTURE
        super.loadIntToUniform(grassTerrainLocation, 0);
        super.loadIntToUniform(mudTerrainLocation, 1);
        super.loadIntToUniform(flowerTerrainLocation, 2);
        super.loadIntToUniform(pathTerrainLocation, 3);
        super.loadIntToUniform(blendMapLocation, 4);
    }

    public void loadSkyColor(Vector3f rgb) {
        super.loadVectorToUniform(skyColorLocation, rgb);
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
