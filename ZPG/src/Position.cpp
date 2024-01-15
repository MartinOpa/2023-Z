#include "headers/Position.h"

Position::Position(glm::vec3 transformation) : Transformation(transformation) {

}

void Position::add(Transformation* transformation) {
    //error
}

glm::mat4 Position::getModelMatrix() {
    glm::mat4 modelMatrix = glm::mat4(1.0f);
    modelMatrix = glm::translate(modelMatrix, this->transformation);
    return modelMatrix;
}
