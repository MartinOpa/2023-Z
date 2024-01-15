#pragma once

#include "IncludeLibs.h"

#include "Light.h"

class DirLight : public Light {
private:
    glm::vec3 direction;
public:
    DirLight(glm::vec3 ambient, glm::vec3 diffuse, glm::vec3 specular, glm::vec3 color, glm::vec3 direction);
    ~DirLight();
    glm::vec3 getDirection() {return this->direction;};
};
