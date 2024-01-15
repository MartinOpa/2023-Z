#include "headers/DirLight.h"

DirLight::DirLight(glm::vec3 ambient, glm::vec3 diffuse, glm::vec3 specular, glm::vec3 color, glm::vec3 direction) : Light(ambient, diffuse, specular, color) {
    this->direction = direction;
}

DirLight::~DirLight() {

}

