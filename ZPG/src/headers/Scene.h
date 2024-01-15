#pragma once

#include <vector>

#include "Shader.h"
#include "Camera.h"
#include "DrawableObject.h"
#include "Skybox.h"

class DrawableObject; class Shader; class Camera; class Skybox;

class Scene {
private:
    std::vector<DrawableObject*> models;
    Shader* shader;
    Camera* camera;
    Skybox* skybox;
    bool hasSkybox;
public:
    Scene(glm::vec3 eye);
    ~Scene();
    void addModel(DrawableObject* model);
    Camera* getCamera();
    void render();
    void toggleFlashlight();
    void setSkybox(Skybox* skybox);
};
