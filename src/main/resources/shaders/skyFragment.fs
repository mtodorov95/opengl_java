#version 400 core

in vec3 textureCoords;
out vec4 color;

uniform samplerCube cubeMap;

void main(){
    color = texture(cubeMap, textureCoords);
}