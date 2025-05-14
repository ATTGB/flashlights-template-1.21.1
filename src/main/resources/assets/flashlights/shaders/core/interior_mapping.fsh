#version 330 core

in vec2 TexCoord;
out vec4 FragColor;

uniform sampler2D exteriorTexture;
uniform sampler2D interiorTexture;

uniform vec3 cameraPos;
uniform vec3 blockPos;
uniform float roomHeight;
uniform float roomDepth;
uniform float roomWidth;

float intersectPlane(vec3 ro, vec3 rd, float planeY) {
    float t = (planeY - ro.y) / rd.y;
    return t > 0.0 ? t : -1.0;
}

void main() {
    vec3 rayOrigin = cameraPos;
    vec3 rayDir = normalize(blockPos - cameraPos);

    float t = intersectPlane(rayOrigin, rayDir, blockPos.y + roomHeight);

    if (t > 0.0) {
        vec3 hitPos = rayOrigin + rayDir * t;
        vec2 uv = fract(hitPos.xz / vec2(roomWidth, roomDepth));

        // Use interior mapping texture for the mapped space
        vec4 wallColor = texture(interiorTexture, uv);
        FragColor = wallColor;
    } else {
        // Use exterior texture if not intersected
        FragColor = texture(exteriorTexture, TexCoord);
    }
}
