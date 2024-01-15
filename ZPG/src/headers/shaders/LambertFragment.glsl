#version 330
out vec4 frag_colour;
in vec3 vn_out;
in vec3 FragPos;
uniform vec3 lightPos;
uniform vec3 viewPos;
uniform vec3 lightColor;
uniform vec3 objectColor;
void main() {
    vec3 ambient = vec3(0.0);
    vec3 norm = normalize(vn_out);
    vec3 lightDir = normalize(lightPos - FragPos);
    float diff = max(dot(norm, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;
    vec3 result = (ambient + diffuse) * objectColor;
    frag_colour = vec4(result, 1.0);
}