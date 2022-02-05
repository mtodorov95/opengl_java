#version 400 core

in vec3 position;
in vec2 textureCoords;
// direction that the surface is facing
in vec3 normal;

out vec2 pass_textureCoords;
// normal, adjusted for the rotation of the entity
out vec3 surfaceNormal;
// vector from the vertex to the light source
out vec3 toLightVector;
// vector to the camera
out vec3 toCameraVector;

// Uniform vars. Used to dynamically interact with the shader code from Java(things like position, lighting, fog,...)
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

uniform float useFakeLighting;

void main(){
    // actual position in the world
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);

    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_textureCoords = textureCoords;

    // if we are faking lighting point the normal vec straight up
    vec3 actualNormal = normal;
    if (useFakeLighting > 0.5){
        actualNormal = vec3(0.0, 1.0, 0.0);
    }

    surfaceNormal = (transformationMatrix * vec4(actualNormal, 0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
    // the view already contains the opposite of the camera position
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
}