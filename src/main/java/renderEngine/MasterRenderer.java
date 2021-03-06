package renderEngine;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyBoxRenderer;
import terrain.Terrain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f; // view distance

    private static final Vector3f SKY_COLOR = new Vector3f(0.5f, 0.5f, 0.5f);

    private Matrix4f projectionMatrix;

    private StaticShader staticShader;
    private TerrainShader terrainShader;
    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private SkyBoxRenderer skyBoxRenderer;

    // hash the textures and reuse them instead of binding, loading and unbinding the same
    // data for N-number of entities
    private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer(Loader loader) {
        enableBackFaceCulling();
        createProjectionMatrix();
        staticShader = new StaticShader();
        terrainShader = new TerrainShader();
        entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        skyBoxRenderer = new SkyBoxRenderer(loader, projectionMatrix);
    }

    public static void enableBackFaceCulling() {
        // Tells openGL to not render certain faces.
        GL11.glEnable(GL11.GL_CULL_FACE);
        // Stops rendering the ones facing back - ones that can't be seen
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void render(List<Light> lights, Camera camera) {
        prepare();
        // Entities
        staticShader.start();
        staticShader.loadSkyColor(SKY_COLOR);
        staticShader.loadLights(lights);
        staticShader.loadViewMatrix(camera);
        entityRenderer.render(entities);
        staticShader.stop();

        // Terrain
        terrainShader.start();
        terrainShader.loadSkyColor(SKY_COLOR);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        terrainRenderer.render(terrains);
        terrainShader.stop();
        //
        skyBoxRenderer.render(camera);
        //
        entities.clear();
        terrains.clear();
    }

    public void processTerrain(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);
        if (batch != null) {
            batch.add(entity);
        } else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) DisplayManager.getWidth() / (float) DisplayManager.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustum_length));
        projectionMatrix.m33(0);
    }

    public void cleanup() {
        staticShader.cleanUp();
        terrainShader.cleanUp();
    }
}
