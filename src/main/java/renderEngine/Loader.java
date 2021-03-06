package renderEngine;

import models.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import textures.TextureData;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();
    private List<Integer> textures = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        // pos=vector3 => coordinateSize = 3
        storeDataInAttributeList(0, 3, positions);
        // textureCoords=vector2 => coordinateSize = 2
        storeDataInAttributeList(1, 2, textureCoords);
        // normals. Vectors perpendicular to the surface at each vertex. Used for lighting
        storeDataInAttributeList(2, 3, normals);
        unbindVAO();
        return new RawModel(vaoID, indices.length);
    }

    // for guis
    public RawModel loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();
        storeDataInAttributeList(0, dimensions, positions);
        unbindVAO();
        return new RawModel(vaoID, positions.length / dimensions);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoID);
        vaos.add(vaoID);
        return vaoID;
    }

    public int loadTexture(String file) {
        int width, height;
        ByteBuffer byteBuffer;
        try {
            MemoryStack stack = MemoryStack.stackPush();
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            String fullPath = "src/main/resources/res/" + file;
            byteBuffer = STBImage.stbi_load(fullPath, w, h, c, 4);
            if (byteBuffer == null) {
                throw new Exception("Unable to load file: " + fullPath);
            }
            width = w.get();
            height = h.get();

            int id = GL11.glGenTextures();
            textures.add(id);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            // Mipmaps
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            // Transition smoothly between resolutions when using mipmaps a.k.a. objects are distant
            // and can be rendered at lower res.
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            // Level of detail. Less noticeable mipmapping. The more negative the number the higher the detail.
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.5f);
            STBImage.stbi_image_free(byteBuffer);
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * @param textureFiles - Order: Right>Left>Top>Bottom>Back>Front
     * @return
     */
    public int loadCubeMap(String[] textureFiles) {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
        for (int i = 0; i < textureFiles.length; i++) {
            TextureData data = decodeTextureFile(textureFiles[i]);
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(),
                    data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        textures.add(texID);
        return texID;
    }

    private TextureData decodeTextureFile(String filename) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            MemoryStack stack = MemoryStack.stackPush();
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            String fullPath = "src/main/resources/res/" + filename;
            buffer = STBImage.stbi_load(fullPath, w, h, c, 4);
            if (buffer == null) {
                throw new Exception("Unable to load file: " + fullPath);
            }
            width = w.get();
            height = h.get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new TextureData(width, height, buffer);
    }

    private void storeDataInAttributeList(int attributeNumber, int coordinatesSize, float[] data) {
        int vboID = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, coordinatesSize, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        vbos.add(vboID);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        // Flip to prep for reading
        buffer.flip();
        return buffer;
    }

    public void cleanUp() {
        for (int vao : vaos) {
            GL30.glDeleteVertexArrays(vao);
        }

        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }

        for (int texture : textures) {
            GL15.glDeleteTextures(texture);
        }
    }
}
