package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import objLoader.ModelData;
import objLoader.OBJFileLoader;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import terrain.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import textures.Texture;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;

public class MainGameLoop {

    private static final int FPS_CAP = 120;

    private static long lastFrameTime;
    private static float delta;

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Loader loader = new Loader();
        ModelData data;
        // Stall
        data = OBJFileLoader.loadOBJ("stall/stall");
        RawModel stallModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        Texture stallTexture = new Texture(loader.loadTexture("stall/stallTexture.png"));
        stallTexture.setShineDamper(50);
        stallTexture.setReflectivity(0.001f);
        TexturedModel textureStallModel = new TexturedModel(stallModel, stallTexture);
        // Trees
        data = OBJFileLoader.loadOBJ("tree/tree");
        RawModel treeModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        Texture treeTexture = new Texture(loader.loadTexture("tree/tree.png"));
        stallTexture.setShineDamper(10);
        stallTexture.setReflectivity(0.001f);
        TexturedModel textureTreeModel = new TexturedModel(treeModel, treeTexture);
        // Fern
        data = OBJFileLoader.loadOBJ("fern/fern");
        RawModel fernModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        Texture fernTexture = new Texture(loader.loadTexture("fern/fern.png"));
        fernTexture.setHasTransparency(true);
        stallTexture.setShineDamper(10);
        stallTexture.setReflectivity(0.001f);
        TexturedModel textureFernModel = new TexturedModel(fernModel, fernTexture);
        // Grasses
        data = OBJFileLoader.loadOBJ("grass/grassModel");
        RawModel grassModel = loader.loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
        Texture grassTexture = new Texture(loader.loadTexture("grass/grassTexture.png"));
        grassTexture.setHasTransparency(true);
        grassTexture.setUseFakeLighting(true);
        stallTexture.setShineDamper(10);
        stallTexture.setReflectivity(0.001f);
        TexturedModel textureGrassModel = new TexturedModel(grassModel, grassTexture);

        // entities
        List<Entity> entities = new ArrayList<>();

        Entity stall = new Entity(textureStallModel, new Vector3f(0, 0, -25), 0, 160, 0, 1);
        Entity stall2 = new Entity(textureStallModel, new Vector3f(-20, 0, -55), 0, 120, 0, 1);
        Entity stall3 = new Entity(textureStallModel, new Vector3f(40, 0, -35), 0, 180, 0, 1);

        entities.add(stall);
        entities.add(stall2);
        entities.add(stall3);

        for (int i = 0; i < 60; i++) {
            float min = -400;
            float x = (float) (Math.random() * 800) + min;
            float z = (float) (Math.random() * 800) + min;
            Entity tree = new Entity(textureTreeModel, new Vector3f(x, 0, z), 0, 0, 0, 10);
            entities.add(tree);
        }

        for (int i = 0; i < 200; i++) {
            float min = -400;
            float x = (float) (Math.random() * 800) + min;
            float z = (float) (Math.random() * 800) + min;
            Entity fern = new Entity(textureFernModel, new Vector3f(x, 0, z), 0, 0, 0, 1);
            entities.add(fern);
        }

        for (int i = 0; i < 500; i++) {
            float min = -400;
            float x = (float) (Math.random() * 800) + min;
            float z = (float) (Math.random() * 800) + min;
            Entity grass = new Entity(textureGrassModel, new Vector3f(x, 0, z), 0, 0, 0, 1);
            entities.add(grass);
        }

        // terrain

        TerrainTexture grass = new TerrainTexture(loader.loadTexture("terrain/grass.png"));
        TerrainTexture mud = new TerrainTexture(loader.loadTexture("terrain/mud.png"));
        TerrainTexture flower = new TerrainTexture(loader.loadTexture("terrain/grassFlowers.png"));
        TerrainTexture path = new TerrainTexture(loader.loadTexture("terrain/path.png"));

        TerrainTexturePack texturePack = new TerrainTexturePack(grass, mud, flower, path);
        TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("terrain/blendMap.png"));

        Terrain terrain = new Terrain(0, 0, loader, texturePack, blendMap, "terrain/heightmap.png");
        Terrain terrain2 = new Terrain(0, -1, loader, texturePack, blendMap, "terrain/heightmap.png");
        Terrain terrain3 = new Terrain(-1, 0, loader, texturePack, blendMap, "terrain/heightmap.png");
        Terrain terrain4 = new Terrain(-1, -1, loader, texturePack, blendMap, "terrain/heightmap.png");

        // light source
        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

        MasterRenderer renderer = new MasterRenderer();

        Player player = new Player(textureTreeModel, new Vector3f(0, 0, -20), 0, 0, 0, 2, DisplayManager.getWindow());

        Camera camera = new Camera(player);

        lastFrameTime = getCurrentTime();

        while (!DisplayManager.isCloseRequested()) {
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            glfwPollEvents();

            long currentFrameTime = getCurrentTime();
            delta = (currentFrameTime - lastFrameTime) / 1000f; // in seconds
            lastFrameTime = currentFrameTime;

            camera.move();
            player.move(delta);
            //
            renderer.processEntity(player);
            //
            renderer.processTerrain(terrain);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);
            //
            // Process all the entities
            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            //
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
            DisplayManager.endFrame();
        }

        renderer.cleanup();
        loader.cleanUp();
        DisplayManager.destroyDisplay();
    }

    private static long getCurrentTime() {
        return System.currentTimeMillis();
    }
}