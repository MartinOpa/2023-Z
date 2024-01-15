#pragma once

#include "IncludeLibs.h"

#include "Subject.h"

class Light : public Subject {
protected:
    glm::vec3 ambient;
    glm::vec3 diffuse;
    glm::vec3 specular;
    glm::vec3 color;
public:
    //Light musi taky posilat notifyObservers("light", this);
    Light(glm::vec3 ambient, glm::vec3 diffuse, glm::vec3 specular, glm::vec3 color) {
        this->ambient = ambient;
        this->diffuse = diffuse;
        this->specular = specular;
        this->color = color;
    };
    ~Light() {};
    glm::vec3 getAmbient() {return this->ambient;};
    glm::vec3 getDiffuse() {return this->diffuse;};
    glm::vec3 getSpecular() {return this->specular;};
    glm::vec3 getColor() {return this->color;};
    void setColor(glm::vec3 color) {this->color = color;};
    void triggerUpdate() {
        notifyObservers("light", this);
    }
};
