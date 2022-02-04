package shaders;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "src/main/resources/shaders/vertex.vs";
    private static final String FRAGMENT_FILE = "src/main/resources/shaders/fragment.fs";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
