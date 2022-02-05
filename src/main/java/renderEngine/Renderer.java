package renderEngine;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import shaders.StaticShader;
import textures.TextureModel;
import tools.Maths;

import java.util.List;
import java.util.Map;

public class Renderer {
    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000.0f; // view distance

    private Matrix4f projectionMatrix;
    private StaticShader shader;

    public Renderer(StaticShader shader) {
        this.shader = shader;
        // Tells openGL to not render certain faces.
        GL11.glEnable(GL11.GL_CULL_FACE);
        // Stops rendering the ones facing back - ones that can't be seen
        GL11.glCullFace(GL11.GL_BACK);

        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void prepare() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClearColor(0, 0, 0, 0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void render(Map<TexturedModel, List<Entity>> entities) {
        for (TexturedModel model : entities.keySet()) {
            prepareTexturedModels(model);
            List<Entity> batch = entities.get(model);
            RawModel rawModel = model.getRawModel();
            for (Entity entity : batch) {
                prepareInstances(entity);
                // The actual rendering
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
    }

    private void prepareTexturedModels(TexturedModel model) {
        RawModel rawModel = model.getRawModel();

        GL30.glBindVertexArray(rawModel.getVaoID());
        // position
        GL20.glEnableVertexAttribArray(0);
        // texture
        GL20.glEnableVertexAttribArray(1);
        // normal
        GL20.glEnableVertexAttribArray(2);
        // Load the shine for the texture
        TextureModel texture = model.getTexture();
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        // Tells opengl which texture to use
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    }

    private void prepareInstances(Entity entity) {
        // Load the entity transformation matrix to the shader.
        // Used to render an N number of entities from one VAO, instead of creating new ones.
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(
                entity.getPosition(),
                entity.getRotX(),
                entity.getRotY(),
                entity.getRotZ(),
                entity.getScale()
        );
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void unbindTexturedModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
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
}
