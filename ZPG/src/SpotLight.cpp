#include "headers/SpotLight.h"

SpotLight::SpotLight(glm::vec3 ambient, glm::vec3 diffuse, glm::vec3 specular, glm::vec3 color,
                     float constant, float linear, float quadratic, float cutOff,
                     float outerCutOff) : Light(ambient, diffuse, specular, color) {
    this->constant = constant;
    this->linear = linear;
    this->quadratic = quadratic;
    this->cutOff = cutOff;
    this->outerCutOff = outerCutOff;
}

SpotLight::~SpotLight() {

}
