#include "headers/Model.h"

Model::Model(char const *fileName, char const *textureFileName, Material* material) {
    int count = 0;
    this->points = loadAssimpModel(fileName, count);
    this->material = material;

    glActiveTexture(GL_TEXTURE0);
    GLuint image = SOIL_load_OGL_texture(textureFileName, SOIL_LOAD_RGBA, SOIL_CREATE_NEW_ID, SOIL_FLAG_INVERT_Y);
    if (image == NULL) {
        std::cout << "An error occurred while loading image." << std::endl;
        exit(EXIT_FAILURE);
    }
    glBindTexture(GL_TEXTURE_2D, image);
    glGenerateMipmap(GL_TEXTURE_2D);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    this->image = image;

    this->VAO = 0;
    this->VBO = 0;

    glGenVertexArrays(1, &this->VAO);
    glBindVertexArray(this->VAO);

    glGenBuffers(1, &this->VBO);
    glBindBuffer(GL_ARRAY_BUFFER, this->VBO);
    glBufferData(GL_ARRAY_BUFFER, this->points.size() * sizeof(float), this->points.data(), GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    glEnableVertexAttribArray(2);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, (sizeof(float) * 8), NULL);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, (sizeof(float) * 8), (void*)(3 * sizeof(float)));
    glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, (sizeof(float) * 8), (void*)(6 * sizeof(float)));
}

Model::Model(std::vector<float> points, Material* material, char const *textureFile) {
    this->points = points;
    this->material = material;

    this->VAO = 0;
    this->VBO = 0;

    glGenVertexArrays(1, &this->VAO);
    glBindVertexArray(this->VAO);

    glGenBuffers(1, &this->VBO);
    glBindBuffer(GL_ARRAY_BUFFER, this->VBO);
    glBufferData(GL_ARRAY_BUFFER, this->points.size() * sizeof(float), this->points.data(), GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    glEnableVertexAttribArray(2);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, (sizeof(float) * 8), NULL);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, (sizeof(float) * 8), (void*)(3 * sizeof(float)));
    glVertexAttribPointer(2, 2, GL_FLOAT, GL_FALSE, (sizeof(float) * 8), (void*)(6 * sizeof(float)));

    unsigned int diffuseMap = loadTexture(textureFile);
    this->diffuseMap = diffuseMap;
}

Model::Model(std::vector<float> points, Material* material) {
    this->diffuseMap = 0;

    this->points = points;
    this->material = material;

    this->VAO = 0;
    this->VBO = 0;

    glGenVertexArrays(1, &this->VAO);
    glBindVertexArray(this->VAO);

    glGenBuffers(1, &this->VBO);
    glBindBuffer(GL_ARRAY_BUFFER, this->VBO);
    glBufferData(GL_ARRAY_BUFFER, this->points.size() * sizeof(float), this->points.data(), GL_STATIC_DRAW);

    glEnableVertexAttribArray(0);
    glEnableVertexAttribArray(1);
    glVertexAttribPointer(0, 3, GL_FLOAT, GL_FALSE, (sizeof(float) * 6), NULL);
    glVertexAttribPointer(1, 3, GL_FLOAT, GL_FALSE, (sizeof(float) * 6), (void*)(3 * sizeof(float)));
}

Model::~Model() {

}

size_t Model::getVertexCount() {
    if (this->diffuseMap == 0) {
        return this->points.size() / 6;
    } else {
        return this->points.size() / 8;
    }
}

void Model::render() {
    glBindVertexArray(this->VAO);

    if (this->image != NULL) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this->image);
    }

    if (this->diffuseMap != 0) {
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, this->diffuseMap);
    }
}

unsigned int Model::loadTexture(char const *textureFile) {
    unsigned int textureID;
    glGenTextures(1, &textureID);

    int width, height, nrComponents;
    unsigned char *data = stbi_load(textureFile, &width, &height, &nrComponents, 0);
    if (data)
    {
        GLenum format;
        if (nrComponents == 1)
            format = GL_RED;
        else if (nrComponents == 3)
            format = GL_RGB;
        else if (nrComponents == 4)
            format = GL_RGBA;

        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexImage2D(GL_TEXTURE_2D, 0, format, width, height, 0, format, GL_UNSIGNED_BYTE, data);
        glGenerateMipmap(GL_TEXTURE_2D);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        stbi_image_free(data);
    }
    else
    {
        std::cout << "Texture failed to load at path: " << textureFile << std::endl;
        stbi_image_free(data);
    }

    return textureID;
}

std::vector<float> Model::loadAssimpModel(const char *fileName, int &count) {
    std::vector<float> data;
    count = 0;
    Assimp::Importer importer;
    unsigned int importOptions = aiProcess_Triangulate
                                 | aiProcess_OptimizeMeshes              // reduce the number of draw calls
                                 | aiProcess_JoinIdenticalVertices       // identifies and joins identical vertex data sets within all imported meshes
                                 | aiProcess_Triangulate                 // triangulates all faces of all meshes
                                 | aiProcess_CalcTangentSpace;           // calculates the tangents and bitangents for the imported meshes

    const aiScene* scene = importer.ReadFile(fileName, importOptions);

    if (scene) {
        aiMesh* mesh = scene->mMeshes[0];//Only first mesh
        printf("Triangles:%d \n", mesh->mNumFaces);
        count = mesh->mNumFaces * 3;

        aiFace* f;
        for (unsigned int i = 0; i < mesh->mNumFaces; i++) {
            aiFace face = mesh->mFaces[i];

            for (unsigned int j = 0; j < 3; j++)
            {
                int id = face.mIndices[j];

                //Vertex position
                aiVector3D pos = mesh->mVertices[id];
                data.push_back(pos.x);
                data.push_back(pos.y);
                data.push_back(pos.z);

                //Vertex normal
                aiVector3D nor = mesh->mNormals[id];
                data.push_back(nor.x);
                data.push_back(nor.y);
                data.push_back(nor.z);

                //Vertex uv
                aiVector3D uv = mesh->mTextureCoords[0][id];
                data.push_back(uv.x);
                data.push_back(uv.y);

            }
        }
    }
    else {
        std::cout << "An error occurred while loading model." << std::endl;
        exit(EXIT_FAILURE);
    };

    return data;
}
