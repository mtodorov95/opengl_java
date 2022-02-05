#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 fragColor;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;

void main(){
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);
    // Used to calc how similar the direction of the two vectors is.
    // The more similar(closer to 1.0) the closer the vertex is to the light source
    // a.k.a. needs to be brighter.
    float nDot1 = dot(unitNormal, unitLightVector);
    // Making the min 0.15 adds a bit of ambient lighting
    float brightness = max(nDot1, 0.15);
    // how much light depending on brightness
    vec3 diffuse = brightness * lightColor;

    vec3 unitVectorToCamera = normalize(toCameraVector);
    // opposite of the toLightVec
    vec3 lightDirection = -unitLightVector;
    // The reflected light comming from the surface
    vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
    // How close is the reflected light direction to where the camera is looking.
    float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
    specularFactor = max(specularFactor, 0.0);
    // how much to damp the reflected light
    float dampedFactor = pow(specularFactor, shineDamper);
    // The final specular(reflected) light, taking the color of the light source into account
    vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

    fragColor = vec4(diffuse, 1.0) * texture(textureSampler, pass_textureCoords) + vec4(finalSpecular, 1.0);
}