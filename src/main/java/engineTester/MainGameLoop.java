package engineTester;

import entities.Camera;
import entities.Entity;
import models.TexturedModel;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import models.RawModel;
import renderEngine.OBJLoader;
import renderEngine.Renderer;
import shaders.StaticShader;
import textures.TextureModel;

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
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        RawModel model = OBJLoader.loadObjModel("src/main/resources/res/stall/stall.obj", loader);
        TextureModel texture = new TextureModel(loader.loadTexture("src/main/resources/res/stall/stallTexture.png"));
        TexturedModel texturedModel = new TexturedModel(model, texture);

        // move to the left
        Entity entity = new Entity(texturedModel, new Vector3f(0,0,-50), 0,0,0,1);

        Camera camera = new Camera(DisplayManager.getWindow());

        while (!DisplayManager.isCloseRequested()){
            entity.increaseRotation(0, 1,0);
            camera.move();
            renderer.prepare();
            shader.start();
            shader.loadViewMatrix(camera);
            renderer.render(entity, shader);
            shader.stop();
            DisplayManager.updateDisplay();
        }

        shader.cleanUp();
        loader.cleanUp();
        DisplayManager.destroyDisplay();
    }
}