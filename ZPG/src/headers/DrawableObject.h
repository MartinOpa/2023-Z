#pragma once

#include "IncludeLibs.h"

#include "Model.h"
#include "Shader.h"
#include "Camera.h"
#include "Transformation.h"

class Shader; class Camera;

class DrawableObject {
private:
    Shader* shader;
    CompositeTransformation* transformation;
    Model* model;
    bool flashlight;
public:
    DrawableObject(std::vector<float> points, Material* material, const char *textureFile, Shader* shader, CompositeTransformation* compositeTransformation);
    DrawableObject(std::vector<float> points, Material* material, Shader* shader, CompositeTransformation* compositeTransformation);
    DrawableObject(const char *fileName, char const *textureFileName, Material* material, Shader* shader, CompositeTransformation* compositeTransformation);
    ~DrawableObject();
    void render();
    void setCamera(Camera* camera);
    Model* getModel() {return this->model;};
    void toggleFlashlight();
};
