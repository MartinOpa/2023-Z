#include "headers/Material.h"

Material::Material(glm::vec3 diffuse, glm::vec3 specular, float shininess) {
    this->diffuse = diffuse;
    this->specular = specular;
    this->shininess = shininess;
}
