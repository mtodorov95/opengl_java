#version 400 core

in vec3 position;
in vec2 textureCoords;

out vec2 pass_textureCoords;

// Uniform vars. Used to dynamically interact with the shader code from Java(things like position, lighting, fog,...)
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main(){
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    pass_textureCoords = textureCoords;
}