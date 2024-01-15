#version 330 core
layout(location=0) in vec3 vp;
layout(location=1) in vec3 vn;
layout(location=2) in vec2 aTexCoords;
out vec3 FragPos;
out vec3 vn_out;
out vec2 TexCoords;
uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    FragPos = vec3(model * vec4(vp, 1.0));
    vn_out = mat3(transpose(inverse(model))) * vn;
    gl_Position = projection * view * vec4(FragPos, 1.0);
    TexCoords = aTexCoords;
}