#include "headers/Scene.h"

Scene::Scene(glm::vec3 eye) {
    this->shader = new Shader();
    this->camera = new Camera(this->shader, eye);
    this->shader->setCamera(this->camera);
    this->hasSkybox = false;
}

Scene::~Scene() {

}

void Scene::addModel(DrawableObject* drawableObject) {
    models.push_back(drawableObject);
}

void Scene::setSkybox(Skybox* skybox) {
    this->skybox = skybox;
    this->hasSkybox = true;
}

Camera* Scene::getCamera() {
    return this->camera;
}

void Scene::render() {
    for (auto model : models) {
        model->setCamera(camera);
        model->render();
    }

    if (this->hasSkybox) { // if (this->skybox != nullptr) {
        this->skybox->setCamera(camera);
        this->skybox->render();
    }
}

void Scene::toggleFlashlight() {
    for (auto model : models) {
        model->toggleFlashlight();
    }
}
