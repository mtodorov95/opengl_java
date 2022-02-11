package shaders;

import entities.Camera;
import entities.Light;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import tools.Maths;

import java.util.List;

public class TerrainShader extends ShaderProgram {

    private static final int MAX_LIGHTS = 4;

    private static final String VERTEX_FILE = "src/main/resources/shaders/terrainVertex.vs";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/terrainFragment.fs";
    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    //    private int lightPositionLocation;
//    private int lightColorLocation;
    private int lightPositionLocation[];
    private int lightColorLocation[];
    private int shineDamperLocation;
    private int reflectivityLocation;
    private int skyColorLocation;
    private int grassTerrainLocation;
    private int mudTerrainLocation;
    private int flowerTerrainLocation;
    private int pathTerrainLocation;
    private int blendMapLocation;
    private int attenuationLocation[];

    public TerrainShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = super.getUniformLocation("transformationMatrix");
        projectionMatrixLocation = super.getUniformLocation("projectionMatrix");
        viewMatrixLocation = super.getUniformLocation("viewMatrix");
//        lightPositionLocation = super.getUniformLocation("lightPosition");
//        lightColorLocation = super.getUniformLocation("lightColor");
        shineDamperLocation = super.getUniformLocation("shineDamper");
        reflectivityLocation = super.getUniformLocation("reflectivity");
        skyColorLocation = super.getUniformLocation("skyColor");

        grassTerrainLocation = super.getUniformLocation("terrainTexture");
        mudTerrainLocation = super.getUniformLocation("terrainTexture2");
        flowerTerrainLocation = super.getUniformLocation("terrainTexture3");
        pathTerrainLocation = super.getUniformLocation("terrainTexture4");
        blendMapLocation = super.getUniformLocation("blendMap");
        //
        lightPositionLocation = new int[MAX_LIGHTS];
        lightColorLocation = new int[MAX_LIGHTS];
        attenuationLocation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            lightPositionLocation[i] = super.getUniformLocation("lightPosition[" + i + "]");
            lightColorLocation[i] = super.getUniformLocation("lightColor[" + i + "]");
            attenuationLocation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
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

//    public void loadLight(Light light) {
//        super.loadVectorToUniform(lightPositionLocation, light.getPosition());
//        super.loadVectorToUniform(lightColorLocation, light.getColor());
//    }

    public void loadLights(List<Light> lights) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVectorToUniform(lightPositionLocation[i], lights.get(i).getPosition());
                super.loadVectorToUniform(lightColorLocation[i], lights.get(i).getColor());
                super.loadVectorToUniform(attenuationLocation[i], lights.get(i).getAttenuation());
            } else {
                // if there are less than max lights
                // fill with empty
                super.loadVectorToUniform(lightPositionLocation[i], new Vector3f(0, 0, 0));
                super.loadVectorToUniform(lightColorLocation[i], new Vector3f(0, 0, 0));
                super.loadVectorToUniform(attenuationLocation[i], new Vector3f(1, 0, 0));
            }
        }
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
