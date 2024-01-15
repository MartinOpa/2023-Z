#include "headers/Transformation.h"

Transformation::Transformation(glm::vec3 transformation) {
    this->transformation = transformation;
}

glm::mat4 CompositeTransformation::getModelMatrix() {
    glm::mat4 modelMatrix = glm::mat4(1.0f);

    for (Transformation* transformation : this->transformations) {
        modelMatrix = modelMatrix * transformation->getModelMatrix();
    }

    return modelMatrix;
}

void CompositeTransformation::add(Transformation* transformation) {
    this->transformations.push_back(transformation);
}
