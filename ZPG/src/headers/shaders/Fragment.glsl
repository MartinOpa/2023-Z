#version 330
out vec4 frag_colour;
in vec3 vn_out;
void main () {
    frag_colour = vec4 (vn_out, 1.0);
}