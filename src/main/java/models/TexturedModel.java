package models;

import textures.TextureModel;

public class TexturedModel {

    private RawModel rawModel;
    private TextureModel texture;

    public TexturedModel(RawModel rawModel, TextureModel texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public TextureModel getTexture() {
        return texture;
    }
}
