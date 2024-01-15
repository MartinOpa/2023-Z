#pragma once

#include "IncludeLibs.h"

#include "Light.h"
#include "Subject.h"

#include <vector>

class PointLight : public Light {
private:
    glm::vec3 position;
    float constant;
    float linear;
    float quadratic;
public:
    PointLight(glm::vec3 ambient, glm::vec3 diffuse, glm::vec3 specular, glm::vec3 color,
               glm::vec3 position, float constant, float linear, float quadratic);
    ~PointLight();
    glm::vec3 getPosition() {return this->position;};
    float getConstant() {return this->constant;};
    float getLinear() {return this->linear;};
    float getQuadratic() {return this->quadratic;};
};

class PointLights : public Subject {
private:
    std::vector<PointLight*> pointLights;
public:
    PointLights();
    ~PointLights();
    void add(PointLight* pointLight);
    std::vector<PointLight*> getPointLights();
    void triggerUpdate() {
        notifyObservers("light", this);
    }
};
