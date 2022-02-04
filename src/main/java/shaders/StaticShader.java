package shaders;

public class StaticShader extends ShaderProgram{

    private static final String VERTEX_FILE = "shaders/vertex.vs";
    private static final String FRAGMENT_FILE = "shaders/fragment.fs";

    public StaticShader() {
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
