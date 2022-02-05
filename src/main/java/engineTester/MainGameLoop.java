package engineTester;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import textures.TextureModel;

import java.util.ArrayList;
import java.util.List;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();

        // This line is critical for LWJGL's interoperation with GLFW's
        // OpenGL context, or any context that is managed externally.
        // LWJGL detects the context that is current in the current thread,
        // creates the GLCapabilities instance and makes the OpenGL
        // bindings available for use.
        GL.createCapabilities();

        Loader loader = new Loader();

        RawModel model = OBJLoader.loadObjModel("src/main/resources/res/stall/stall.obj", loader);
        TextureModel texture = new TextureModel(loader.loadTexture("src/main/resources/res/stall/stallTexture.png"));
        texture.setShineDamper(50);
        texture.setReflectivity(0.001f);

        TexturedModel texturedModel = new TexturedModel(model, texture);

        //
        Entity stall = new Entity(texturedModel, new Vector3f(0, 0, -25), 0, 160, 0, 1);
        Entity stall2 = new Entity(texturedModel, new Vector3f(-20, 0, -55), 0, 120, 0, 1);
        Entity stall3 = new Entity(texturedModel, new Vector3f(40, 0, -35), 0, 180, 0, 1);

        List<Entity> entities = new ArrayList<>();
        entities.add(stall);
        entities.add(stall2);
        entities.add(stall3);
        //
        // light source
        Light light = new Light(new Vector3f(3000, 2000, 2000), new Vector3f(1, 1, 1));

        Camera camera = new Camera(DisplayManager.getWindow());

        MasterRenderer renderer = new MasterRenderer();


        while (!DisplayManager.isCloseRequested()) {
            camera.move();
            // Process all the entities
            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }
            //
            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanup();
        loader.cleanUp();
        DisplayManager.destroyDisplay();
    }
}