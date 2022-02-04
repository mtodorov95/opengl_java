#version 400 core

in vec2 pass_textureCoords;

out vec4 fragColor;

uniform sampler2D textureSampler;

void main(){
    fragColor = texture(textureSampler, pass_textureCoords);
}