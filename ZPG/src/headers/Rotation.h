#pragma once

#include "Transformation.h"

class Rotation : public Transformation {
private:
    glm::vec3 axis;
    float angle; // deprec. degrees
public:
    Rotation(glm::vec3 axis, float angle);
    glm::mat4 getModelMatrix() override;
    void add(Transformation* transformation) override;
};
