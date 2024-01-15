#pragma once

#include "IncludeLibs.h"

#include "SceneCreator.h"
#include "Scene.h"
#include "Model.h"
#include "Transformation.h"
#include "Rotation.h"
#include "Scale.h"
#include "Position.h"

class Scene; class SceneCreator;

class Application {
private:
    GLFWwindow* window;
    Scene* scene;
    SceneCreator* sceneCreator;
    bool inputModeDisabled;
    float deltaTime = 0.0f;
    float lastFrame = 0.0f;
public:
    ~Application();
    void initialization();
    void createModels();
    void run();
    static Application& getInstance();
    float getDeltaTime();

    static void error_callback(int error, const char* description);
    static void key_callback(GLFWwindow* window, int key, int scancode, int action, int mods);
    static void window_focus_callback(GLFWwindow* window, int focused);
    static void window_iconify_callback(GLFWwindow* window, int iconified);
    static void window_size_callback(GLFWwindow* window, int width, int height);
    static void cursor_callback(GLFWwindow* window, double x, double y);
    static void button_callback(GLFWwindow* window, int button, int action, int mode);
};