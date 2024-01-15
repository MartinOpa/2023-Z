#include "headers/Shader.h"

Shader::Shader(const char *vertexShaderType, const char *fragmentShaderType) {
    this->first = true;
    ShaderLoader* shaderLoader = new ShaderLoader();
    this->shaderLoader = shaderLoader;
    this->shaderProgram = this->shaderLoader->loadShader(vertexShaderType, fragmentShaderType);
    this->skybox = false;
}

Shader::Shader() {
    this->first = true;
    ShaderLoader* shaderLoader = new ShaderLoader();
    this->shaderLoader = shaderLoader;
    this->shaderProgram = this->shaderLoader->loadShader("../src/headers/shaders/Vertex.glsl", "../src/headers/shaders/Fragment.glsl");
    this->skybox = false;
}

Shader::~Shader() {

}

void Shader::setSkybox() {
    this->skybox = true;
}

void Shader::useShader() {
    glUseProgram(this->shaderProgram);
    setUniformVar("objectColor", this->color);
    setUniformVar("viewPos", this->camera->getEye());
}

void Shader::setModelViewProjectionMatrix(Transformation* transformation) {
    setUniformVar("model", transformation->getModelMatrix());
}

void Shader::setCamera(Camera* camera) {
    this->camera = camera;
    if (this->first) {
        this->first = false;
        this->camera->subscribe(this);
    }
}

void Shader::update(Subject* sender) {
    if (sender == this->camera) {
        setUniformVar("projection", this->camera->getPerspective());
        if (this->skybox) {
            setUniformVar("view", glm::mat4(glm::mat3(this->camera->getCamera())));
        } else {
            setUniformVar("view", this->camera->getCamera());
        }
    }

    /*if (sender == this->spotLight) {
        setUniformVar("spotLight.position", this->camera->getEye());
        //setUniformVar("spotLight.color", this->spotLight->getColor());
        setUniformVar("spotLight.direction", this->camera->getTarget());
        setUniformVar("spotLight.ambient", this->spotLight->getAmbient());
        setUniformVar("spotLight.diffuse", this->spotLight->getDiffuse());
        setUniformVar("spotLight.specular", this->spotLight->getSpecular());
        setUniformVar("spotLight.constant", this->spotLight->getConstant());
        setUniformVar("spotLight.linear", this->spotLight->getLinear());
        setUniformVar("spotLight.quadratic", this->spotLight->getQuadratic());
        setUniformVar("spotLight.cutOff", this->spotLight->getCutOff());
        setUniformVar("spotLight.outerCutOff", this->spotLight->getOuterCutOff());
    }

    if (sender == this->dirLight) {
        //setUniformVar("dirLight.color", this->dirLight->getColor());
        setUniformVar("dirLight.direction", this->dirLight->getDirection());
        setUniformVar("dirLight.ambient", this->dirLight->getAmbient());
        setUniformVar("dirLight.diffuse", this->dirLight->getDiffuse());
        setUniformVar("dirLight.specular", this->dirLight->getSpecular());
    }

    if (sender == this->pointLights) {
        setUniformVars(this->pointLights->getPointLights());
    }*/
}

void Shader::updateLights() {
    if (this->spotLight != nullptr) {
        setUniformVar("spotLight.color", this->spotLight->getColor());
        setUniformVar("spotLight.position", this->camera->getEye());
        setUniformVar("spotLight.direction", this->camera->getTarget());
        setUniformVar("spotLight.ambient", this->spotLight->getAmbient());
        setUniformVar("spotLight.diffuse", this->spotLight->getDiffuse());
        setUniformVar("spotLight.specular", this->spotLight->getSpecular());
        setUniformVar("spotLight.constant", this->spotLight->getConstant());
        setUniformVar("spotLight.linear", this->spotLight->getLinear());
        setUniformVar("spotLight.quadratic", this->spotLight->getQuadratic());
        setUniformVar("spotLight.cutOff", this->spotLight->getCutOff());
        setUniformVar("spotLight.outerCutOff", this->spotLight->getOuterCutOff());
    }

    if (this->dirLight != nullptr) {
        setUniformVar("dirLight.color", this->dirLight->getColor());
        setUniformVar("dirLight.direction", this->dirLight->getDirection());
        setUniformVar("dirLight.ambient", this->dirLight->getAmbient());
        setUniformVar("dirLight.diffuse", this->dirLight->getDiffuse());
        setUniformVar("dirLight.specular", this->dirLight->getSpecular());
    }

    if (this->pointLights != nullptr) {
        setUniformVars(this->pointLights->getPointLights());
    }
}

void Shader::updateMaterial(Material* material) {
    setUniformVar("material.diffuse", material->getDiffuse());
    setUniformVar("material.specular", material->getSpecular());
    setUniformVar("material.shininess", material->getShininess());
}

void Shader::setUniformVar(const char* name, glm::mat4 matrix) {
    GLuint matrixID = glGetUniformLocation(this->shaderProgram, name);
    if (matrixID != -1) {
        glProgramUniformMatrix4fv(this->shaderProgram, matrixID, 1, GL_FALSE, glm::value_ptr(matrix));
    }
}

void Shader::setUniformVar(const char* name, glm::vec3 vector) {
    GLuint vectorID = glGetUniformLocation(this->shaderProgram, name);
    if (vectorID != -1) {
        glProgramUniform3fv(this->shaderProgram, vectorID, 1, glm::value_ptr(vector));
    }
}

void Shader::setUniformVar(const char* name, float value) {
    GLuint vectorID = glGetUniformLocation(this->shaderProgram, name);
    if (vectorID != -1) {
        glProgramUniform1f(this->shaderProgram, vectorID, value);
    }
}

void Shader::setUniformVars(std::vector<PointLight*> lights) {
    int i = 0;
    for (auto light : lights) {
        setUniformVar(("pointLights[" + std::to_string(i) + "].position").c_str(), light->getPosition());
        setUniformVar(("pointLights[" + std::to_string(i) + "].color").c_str(), light->getColor());
        setUniformVar(("pointLights[" + std::to_string(i) + "].ambient").c_str(), light->getAmbient());
        setUniformVar(("pointLights[" + std::to_string(i) + "].diffuse").c_str(), light->getDiffuse());
        setUniformVar(("pointLights[" + std::to_string(i) + "].specular").c_str(), light->getSpecular());
        setUniformVar(("pointLights[" + std::to_string(i) + "].constant").c_str(), light->getConstant());
        setUniformVar(("pointLights[" + std::to_string(i) + "].linear").c_str(), light->getLinear());
        setUniformVar(("pointLights[" + std::to_string(i) + "].quadratic").c_str(), light->getQuadratic());
        i++;
    }
}

void Shader::setPointLights(PointLights* pointLights) {
    this->pointLights = pointLights;
    this->pointLights->subscribe(this);
}

void Shader::setSpotLight(SpotLight* spotLight) {
    this->spotLight = spotLight;
    this->spotLight->subscribe(this);
}

void Shader::setDirLight(DirLight* dirLight) {
    this->dirLight = dirLight;
    this->dirLight->subscribe(this);
}

void Shader::toggleFlashlight(bool on) {
    if (on) {
        this->spotLight->setColor(glm::vec3(1.f, 1.f, 1.f));
    } else {
        this->spotLight->setColor(glm::vec3(0.f, 0.f, 0.f));
    }
}
