#include "headers/PointLight.h"

PointLight::PointLight(glm::vec3 ambient, glm::vec3 diffuse, glm::vec3 specular, glm::vec3 color,
                       glm::vec3 position, float constant, float linear, float quadratic) : Light(ambient, diffuse, specular, color) {
    this->position = position;
    this->constant = constant;
    this->linear = linear;
    this->quadratic = quadratic;
}

PointLight::~PointLight() {

}

PointLights::PointLights() {

}

PointLights::~PointLights() {

}

void PointLights::add(PointLight* pointLight) {
    this->pointLights.push_back(pointLight);
}

std::vector<PointLight*> PointLights::getPointLights() {
    return this->pointLights;
}
