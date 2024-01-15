#version 330 core
out vec4 frag_colour;
in vec3 vn_out;
in vec3 FragPos;
uniform vec3 ambientColor;
uniform vec3 objectColor;
void main() {
    vec3 ambient = ambientColor;
    vec3 result = objectColor;
    frag_colour = vec4(result, 1.0);
}