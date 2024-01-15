#pragma once

#include "Transformation.h"

class Position : public Transformation {
private:
    glm::vec3 position;
public:
    Position(glm::vec3 transformation);
    glm::mat4 getModelMatrix() override;
    void add(Transformation* transformation) override;
};
