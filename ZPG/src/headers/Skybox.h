#pragma once

#include "IncludeLibs.h"

#include "Shader.h"
#include "Camera.h"

#include <stb_image.h>

#include <iostream>
#include <vector>
#include <string>

class Shader; class Camera;

class Skybox {
private:
    Shader* shader;
    GLuint VBO;
    GLuint VAO;
    std::vector<float> points;
    unsigned int cubemapTexture;
    unsigned int loadCubemap(std::vector<std::string> faces);
public:
    Skybox(Shader* shader, std::string dir, std::vector<float> points);
    ~Skybox();
    void render();
    void setCamera(Camera* camera);
};
