#include "headers/Rotation.h"

Rotation::Rotation(glm::vec3 axis, float angle) {
    this->axis = axis;
    this->angle = angle;
}

void Rotation::add(Transformation* transformation) {
    //error
}

glm::mat4 Rotation::getModelMatrix() {
    glm::mat4 modelMatrix = glm::mat4(1.0f);
    modelMatrix = glm::rotate(modelMatrix, this->angle, this->axis);
    return modelMatrix;
}
