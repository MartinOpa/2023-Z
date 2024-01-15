#pragma once

#include "IncludeLibs.h"

class Material {
private:
    glm::vec3 diffuse;
    glm::vec3 specular;
    float shininess;
public:
    Material(glm::vec3 diffuse, glm::vec3 specular, float shininess);
    ~Material() {};
    glm::vec3 getDiffuse(){return this->diffuse;};
    glm::vec3 getSpecular() {return this->specular;};
    float getShininess() {return this->shininess;};
};
