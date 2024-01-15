#pragma once

#include "IncludeLibs.h"

#include "Subject.h"
#include "Application.h"
#include "Shader.h"

class Shader; // dopredna deklarace (z duvodu krizoveho odkazu)
class Camera : public Subject {
private:
    Shader* shader;
    glm::vec3 eye;
    glm::vec3 target;
    glm::vec3 up;
    glm::mat4 perspective;
    float speed = 45.0f;
    float yaw = -90.0f;
    float pitch = 0.0f;
    bool firstMouse = true;
    float lastX = 0.0f;
    float lastY = 0.0f;
public:
    Camera(Shader* shader, glm::vec3 eye);
    ~Camera();
    glm::mat4 getCamera();
    glm::mat4 getPerspective() {return this->perspective;};
    glm::vec3 getEye() {return this->eye;};
    glm::vec3 getTarget() {return this->target;};
    void moveForward();
    void moveBackward();
    void moveLeft();
    void moveRight();
    void mouseMove(double xpos, double ypos);
    void windowResize(int width, int height);
};

