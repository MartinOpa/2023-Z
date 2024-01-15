#pragma once

#include "IncludeLibs.h"
#include <stb_image.h>
#include <soil2/SOIL2.h>

#include "Material.h"

#include <iostream>
#include <vector>

#include <assimp/Importer.hpp>// C++ importerinterface
#include <assimp/scene.h>// aiSceneoutputdata structure
#include <assimp/postprocess.h>// Post processingflags

class Model {
private:
    GLuint VBO;
    GLuint VAO;
    std::vector<float> points;
    Material* material;
    unsigned int diffuseMap;
    GLuint image;
    unsigned int loadTexture(char const *textureFile);
    std::vector<float> loadAssimpModel(const char *fileName, int &count);
public:
    Model(std::vector<float> points, Material* material, char const *textureFile);
    Model(std::vector<float> points, Material* material);
    Model(char const *fileName, char const *textureFileName, Material* material);
    ~Model();
    void render();
    size_t getVertexCount();
    Material* getMaterial() {return this->material;};
    unsigned int getDiffuseMap() {return this->diffuseMap;};
};
