#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
// in vec3 toLightVector;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 fragColor;

uniform sampler2D textureSampler;
//uniform vec3 lightColor;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(){
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    // Sum up all light sources
    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for (int i=0;i<4;i++){
        float distance = length(toLightVector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
        //
        vec3 unitLightVector = normalize(toLightVector[i]);
        // Used to calc how similar the direction of the two vectors is.
        // The more similar(closer to 1.0) the closer the vertex is to the light source
        // a.k.a. needs to be brighter.
        float nDot1 = dot(unitNormal, unitLightVector);
        // Making the min > 0 adds ambient lighting
        float brightness = max(nDot1, 0.0);
        // opposite of the toLightVec
        vec3 lightDirection = -unitLightVector;
        // The reflected light comming from the surface
        vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
        // How close is the reflected light direction to where the camera is looking.
        float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);
        // how much to damp the reflected light
        float dampedFactor = pow(specularFactor, shineDamper);
        // how much light depending on brightness
        totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attFactor;
        // The final specular(reflected) light, taking the color of the light source into account
        totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i]) / attFactor;
    }
    // Ambient
    totalDiffuse = max(totalDiffuse, 0.15);

    vec4 textureColor = texture(textureSampler, pass_textureCoords);
    // Easy check to see if the texture is transparent
    // if so don't render it at all
    if (textureColor.a < 0.5){
        discard;
    }

    fragColor = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    // Add the sky color depending on distance = fog
    fragColor = mix(vec4(skyColor, 1.0), fragColor, visibility);
}