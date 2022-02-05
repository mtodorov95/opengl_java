#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main(){
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    // Used to calc how similar the direction of the two vectors is.
    // The more similar(closer to 1.0) the closer the vertex is to the light source
    // a.k.a. needs to be brighter.
    float nDot1 = dot(unitNormal, unitLightVector);
    float brightness = max(nDot1, 0.0);
    // how much light depending on brightness
    vec3 diffuse = brightness * lightColor;

    fragColor = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords);
}