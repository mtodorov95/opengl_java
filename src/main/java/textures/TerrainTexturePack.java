package textures;

public class TerrainTexturePack {

    private TerrainTexture grassTexture; // back
    private TerrainTexture mudTexture; // r
    private TerrainTexture flowersTexture; // g
    private TerrainTexture pathTexture; // b

    public TerrainTexturePack(TerrainTexture grassTexture, TerrainTexture mudTexture, TerrainTexture flowersTexture, TerrainTexture pathTexture) {
        this.grassTexture = grassTexture;
        this.mudTexture = mudTexture;
        this.flowersTexture = flowersTexture;
        this.pathTexture = pathTexture;
    }

    public TerrainTexture getGrassTexture() {
        return grassTexture;
    }

    public TerrainTexture getMudTexture() {
        return mudTexture;
    }

    public TerrainTexture getFlowersTexture() {
        return flowersTexture;
    }

    public TerrainTexture getPathTexture() {
        return pathTexture;
    }
}
