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
import textures.Texture;
import tools.Maths;

import java.util.List;
import java.util.Map;

public class EntityRenderer {

    private StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
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
        Texture texture = model.getTexture();
        // Disable culling for textures with transparency
        if (texture.isHasTransparency()) {
            MasterRenderer.disableCulling();
        }
        shader.loadFakeLightingVar(texture.isUseFakeLighting());
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
        MasterRenderer.enableBackFaceCulling();

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
