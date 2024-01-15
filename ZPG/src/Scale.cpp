#include "headers/Scale.h"

Scale::Scale(glm::vec3 transformation) : Transformation(transformation) {

}

void Scale::add(Transformation* transformation) {
    //error
}

glm::mat4 Scale::getModelMatrix() {
    glm::mat4 modelMatrix = glm::mat4(1.0f);
    modelMatrix = glm::scale(modelMatrix, this->transformation);
    return modelMatrix;
}
