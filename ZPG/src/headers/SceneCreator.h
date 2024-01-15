#pragma once

#include "IncludeLibs.h"

#include "Scene.h"
#include "Shader.h"
#include "SpotLight.h"
#include "DirLight.h"
#include "PointLight.h"
#include "Skybox.h"

#include <vector>

class Scene;

class SceneCreator {
private:
    std::vector<Scene*> scenes;
    int current;
public:
    SceneCreator();
    ~SceneCreator();
    Scene* getNext();
    Scene* getFirst();
};
