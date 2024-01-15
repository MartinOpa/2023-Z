#pragma once

#include "IncludeLibs.h"

#include "Light.h"

class SpotLight : public Light {
private:
    float constant;
    float linear;
    float quadratic;
    float cutOff;
    float outerCutOff;
public:
    SpotLight(glm::vec3 ambient, glm::vec3 diffuse, glm::vec3 specular, glm::vec3 color,
              float constant, float linear, float quadratic, float cutOff, float outerCutOff);
    ~SpotLight();
    float getConstant() {return this->constant;};
    float getLinear() {return this->linear;};
    float getQuadratic() {return this->quadratic;};
    float getCutOff() {return this->cutOff;};
    float getOuterCutOff() {return this->outerCutOff;};
};
