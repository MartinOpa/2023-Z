#include "headers/Skybox.h"

Skybox::Skybox(Shader* shader, std::string dir, std::vector<float> points) {
    this->shader = shader;
    this->points = points;

    this->VAO = 0;
    this->VBO = 0;

    glGenVertexArrays(1, &this->VAO );
    glGenBuffers(1, &this->VBO);
    glBindVertexArray(this->VAO );
    glBindBuffer(GL_ARRAY_BUFFER, this->VAO);
    glBufferData(GL_ARRAY_BUFFER, this->points.size() * sizeof(float), this->points.data(), GL_STATIC_DRAW);
    glEnableVertexAttribArray(0);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, (sizeof(float) * 3), NULL);

    std::vector<std::string> faces = {
        dir + "posx.jpg",
        dir + "negx.jpg",
        dir + "posy.jpg",
        dir + "negy.jpg",
        dir + "posz.jpg",
        dir + "negz.jpg"
    };

    unsigned int cubemapTexture = loadCubemap(faces);
    this->cubemapTexture = cubemapTexture;
}

void Skybox::setCamera(Camera* camera) {
    this->shader->setCamera(camera);
}

void Skybox::render() {
    glDepthFunc(GL_LEQUAL);
    this->shader->useShader();
    this->shader->setSkybox();

    glBindVertexArray(this->VAO);
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_CUBE_MAP, this->cubemapTexture);
    glDrawArrays(GL_TRIANGLES, 0, this->points.size()/3);
    glBindVertexArray(0);
    glDepthFunc(GL_LESS);
}

unsigned int Skybox::loadCubemap(std::vector<std::string> faces) {
    unsigned int textureID;
    glGenTextures(1, &textureID);
    glBindTexture(GL_TEXTURE_CUBE_MAP, textureID);

    int width, height, nrChannels;
    for (unsigned int i = 0; i < faces.size(); i++)
    {
        unsigned char *data = stbi_load(faces[i].c_str(), &width, &height, &nrChannels, 0);
        if (data)
        {
            glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, data);
            stbi_image_free(data);
        }
        else
        {
            std::cout << "Cubemap texture failed to load at path: " << faces[i] << std::endl;
            stbi_image_free(data);
        }
    }
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

    return textureID;
}