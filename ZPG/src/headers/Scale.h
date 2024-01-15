#pragma once

#include "Transformation.h"

class Scale : public Transformation {
private:
    glm::vec3 scale;
public:
    Scale(glm::vec3 transformation);
    glm::mat4 getModelMatrix() override;
    void add(Transformation* transformation) override;
};
