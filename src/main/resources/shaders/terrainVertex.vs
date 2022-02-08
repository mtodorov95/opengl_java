#version 400 core

in vec3 position;
in vec2 textureCoords;
// direction that the surface is facing
in vec3 normal;

out vec2 pass_textureCoords;
// normal, adjusted for the rotation of the entity
out vec3 surfaceNormal;
// vector from the vertex to the light source
// out vec3 toLightVector;
// multiple light sources
out vec3 toLightVector[4];
// vector to the camera
out vec3 toCameraVector;
//
out float visibility;

// Uniform vars. Used to dynamically interact with the shader code from Java(things like position, lighting, fog,...)
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
// single source
// uniform vec3 lightPosition;
uniform vec3 lightPosition[4];

// Fog
const float density = 0.003;
const float gradient = 5.0;

void main(){
    // actual position in the world
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    //
    vec4 positionRelativeToCamera = viewMatrix * worldPosition;

    gl_Position = projectionMatrix * positionRelativeToCamera;

    pass_textureCoords = textureCoords;

    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;
    // single
    // toLightVector = lightPosition - worldPosition.xyz;
    for (int i=0;i<4;i++){
        toLightVector[i] = lightPosition[i] - worldPosition.xyz;
    }
    // the view already contains the opposite of the camera position
    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
    //
    float distance = length(positionRelativeToCamera.xyz);
    visibility = exp(-pow((distance*density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);
}