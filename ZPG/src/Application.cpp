#include "headers/Application.h"

Application& Application::getInstance() {
    static Application app;
    return app;
}

Application::~Application() {

}

float Application::getDeltaTime() {
    return deltaTime;
}

void Application::initialization() {
    glfwSetErrorCallback(error_callback);
    if (!glfwInit()) {
        fprintf(stderr, "ERROR: could not start GLFW3\n");
        exit(EXIT_FAILURE);
    }

    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
    glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);

    window = glfwCreateWindow(1024, 768, "ZPG - OPA0023", NULL, NULL);
    if (!window) {
        glfwTerminate();
        exit(EXIT_FAILURE);
    }

    glfwMakeContextCurrent(window);
    glfwSwapInterval(1);

    glewExperimental = GL_TRUE;
    glewInit();

    glfwSetKeyCallback(window, key_callback);

    glfwSetCursorPosCallback(window, cursor_callback);

    glfwSetMouseButtonCallback(window, button_callback);

    glfwSetWindowFocusCallback(window, window_focus_callback);

    glfwSetWindowIconifyCallback(window, window_iconify_callback);

    glfwSetWindowSizeCallback(window, window_size_callback);

    glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
    inputModeDisabled = true;

    glEnable(GL_DEPTH_TEST);

    printf("OpenGL Version: %s\n", glGetString(GL_VERSION));
    printf("Using GLEW %s\n", glewGetString(GLEW_VERSION));
    printf("Vendor %s\n", glGetString(GL_VENDOR));
    printf("Renderer %s\n", glGetString(GL_RENDERER));
    printf("GLSL %s\n", glGetString(GL_SHADING_LANGUAGE_VERSION));

    int major, minor, revision;
    glfwGetVersion(&major, &minor, &revision);
    printf("Using GLFW %i.%i.%i\n", major, minor, revision);

    int width, height;
    glfwGetFramebufferSize(window, &width, &height);
    glViewport(0, 0, width, height);
}

void Application::createModels() {
    SceneCreator* sceneCreator = new SceneCreator();
    this->sceneCreator = sceneCreator;
    scene = sceneCreator->getFirst();
}

void Application::run() {
    while (!glfwWindowShouldClose(window)) {
        float currentTime = glfwGetTime();
        deltaTime = currentTime - lastFrame;
        lastFrame = currentTime;

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        scene->render();
        glfwPollEvents();
        glfwSwapBuffers(window);
    }

    glfwDestroyWindow(window);
    glfwTerminate();
    exit(EXIT_SUCCESS);
}

void Application::error_callback(int error, const char* description) {
    fputs(description, stderr);
}

void Application::key_callback(GLFWwindow* window, int key, int scancode, int action, int mods) {
    if (key == GLFW_KEY_ESCAPE && action == GLFW_PRESS) {
        glfwSetWindowShouldClose(window, GL_TRUE);
    }

    if (Application::getInstance().inputModeDisabled && key == GLFW_KEY_W && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
        Application::getInstance().scene->getCamera()->moveForward();
    }

    if (Application::getInstance().inputModeDisabled && key == GLFW_KEY_S && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
        Application::getInstance().scene->getCamera()->moveBackward();
    }

    if (Application::getInstance().inputModeDisabled && key == GLFW_KEY_A && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
        Application::getInstance().scene->getCamera()->moveLeft();
    }

    if (Application::getInstance().inputModeDisabled && key == GLFW_KEY_D && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
        Application::getInstance().scene->getCamera()->moveRight();
    }

    if (key == GLFW_KEY_Q && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
        Application::getInstance().scene = Application::getInstance().sceneCreator->getNext();
    }

    if (key == GLFW_KEY_E && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
        if (Application::getInstance().inputModeDisabled) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
            Application::getInstance().inputModeDisabled = false;
        } else {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
            Application::getInstance().inputModeDisabled = true;
        }
    }

    if (Application::getInstance().inputModeDisabled && key == GLFW_KEY_F && (action == GLFW_PRESS || action == GLFW_REPEAT)) {
        Application::getInstance().scene->toggleFlashlight();
    }

    printf("key_callback [%d,%d,%d,%d] \n", key, scancode, action, mods);
}

void Application::window_focus_callback(GLFWwindow* window, int focused) {
    printf("window_focus_callback \n");
}

void Application::window_iconify_callback(GLFWwindow* window, int iconified) {
    printf("window_iconify_callback \n");
}

void Application::window_size_callback(GLFWwindow* window, int width, int height) {
    printf("resize %d, %d \n", width, height);
    glViewport(0, 0, width, height);
    Application::getInstance().scene->getCamera()->windowResize(width, height);
}

void Application::cursor_callback(GLFWwindow* window, double x, double y) {
    printf("cursor_callback \n");
    if (Application::getInstance().inputModeDisabled) {
        Application::getInstance().scene->getCamera()->mouseMove(x, y);
    }
}

void Application::button_callback(GLFWwindow* window, int button, int action, int mode) {
    //
}
