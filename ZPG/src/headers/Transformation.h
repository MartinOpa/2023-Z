#pragma once

#include "IncludeLibs.h"

#include <vector>

class Transformation {
protected:
    Transformation() {};
    Transformation(glm::vec3 transformation);
public:
    ~Transformation() = default;
    glm::vec3 transformation;
    virtual glm::mat4 getModelMatrix() = 0;
    virtual void add(Transformation* transformation) = 0;
};

class CompositeTransformation : public Transformation {
private:
    std::vector<Transformation*> transformations;
public:
    CompositeTransformation() {};
    glm::mat4 getModelMatrix() override;
    void add(Transformation* transformation) override;
};
