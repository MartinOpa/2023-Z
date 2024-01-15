#include "headers/DrawableObject.h"

DrawableObject::DrawableObject(std::vector<float> points, Material* material, const char *textureFile, Shader* shader, CompositeTransformation* compositeTransformation) {
    this->model = new Model(points, material, textureFile);
    this->shader = shader;
    this->transformation = compositeTransformation;
    this->flashlight = false;
}

DrawableObject::DrawableObject(std::vector<float> points, Material* material, Shader* shader, CompositeTransformation* compositeTransformation) {
    this->model = new Model(points, material);
    this->shader = shader;
    this->transformation = compositeTransformation;
    this->flashlight = false;
}

DrawableObject::DrawableObject(const char *fileName, char const *textureFileName, Material* material, Shader* shader, CompositeTransformation* compositeTransformation) {
    this->model = new Model(fileName, textureFileName, material);
    this->shader = shader;
    this->transformation = compositeTransformation;
    this->flashlight = false;
}

DrawableObject::~DrawableObject() {

}

void DrawableObject::render() {
    this->shader->useShader();
    this->shader->updateMaterial(this->model->getMaterial());
    this->shader->updateLights();
    this->shader->setModelViewProjectionMatrix(this->transformation);
    this->model->render();
    glDrawArrays(GL_TRIANGLES, 0, this->model->getVertexCount());
}

void DrawableObject::setCamera(Camera* camera) {
    this->shader->setCamera(camera);
}

void DrawableObject::toggleFlashlight() {
    this->flashlight = !this->flashlight;
    this->shader->toggleFlashlight(this->flashlight);
}
