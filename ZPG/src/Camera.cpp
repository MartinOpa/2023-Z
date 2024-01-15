#include "headers/Camera.h"

Camera::Camera(Shader* shader, glm::vec3 eye) {
    this->shader = shader;
    this->eye = eye;
    this->target = glm::vec3(0.0f, 0.0f, -1.0f);
    this->up = glm::vec3(0.0f, 1.0f, 0.0f);
    this->perspective = glm::perspective(glm::radians(60.0f), 4.0f / 3.0f, 0.1f, 100.0f);
}

glm::mat4 Camera::getCamera() {
    return glm::lookAt(eye, eye + target, up);
}

void Camera::moveForward() {
    this->eye += this->target * (this->speed * Application::getInstance().getDeltaTime());
    notifyObservers("view", this);
}

void Camera::moveBackward() {
    this->eye -= this->target * (this->speed * Application::getInstance().getDeltaTime());
    notifyObservers("view", this);
}

void Camera::moveLeft() {
    this->eye -= glm::normalize(glm::cross(this->target, this->up)) * (this->speed * Application::getInstance().getDeltaTime());
    notifyObservers("view", this);
}

void Camera::moveRight() {
    this->eye += glm::normalize(glm::cross(this->target, this->up)) * (this->speed * Application::getInstance().getDeltaTime());
    notifyObservers("view", this);
}

void Camera::mouseMove(double xpos, double ypos) {
    if (this->firstMouse) {
        this->lastX = xpos;
        this->lastY = ypos;
        this->firstMouse = false;
    }
    float xoffset = xpos - this->lastX;
    float yoffset = this->lastY - ypos;
    this->lastX = xpos;
    this->lastY = ypos;
    float sensitivity = 0.1f;
    xoffset *= sensitivity;
    yoffset *= sensitivity;
    this->yaw += xoffset;
    this->pitch += yoffset;
    if (this->pitch > 89.0f)
        this->pitch = 89.0f;
    if (this->pitch < -89.0f)
        this->pitch = -89.0f;
    glm::vec3 front;
    front.x = cos(glm::radians(this->yaw)) * cos(glm::radians(this->pitch));
    front.y = sin(glm::radians(this->pitch));
    front.z = sin(glm::radians(this->yaw)) * cos(glm::radians(this->pitch));
    this->target = glm::normalize(front);

    notifyObservers("view", this);
}

void Camera::windowResize(int width, int height) {
    this->perspective = glm::perspective(glm::radians(60.0f), (float)width / (float)height, 0.1f, 100.0f);
    notifyObservers("projection", this);
}

Camera::~Camera() {
    delete shader;
}
