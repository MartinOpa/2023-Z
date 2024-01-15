#pragma once

#include "IncludeLibs.h"

#include "ShaderLoader.h"
#include "Camera.h"
#include "Transformation.h"
#include "PointLight.h"
#include "SpotLight.h"
#include "DirLight.h"
#include "Material.h"

#include <string>

class Camera; // dopredna deklarace (z duvodu krizoveho odkazu)
class Shader : public Observer {
private:
    GLuint shaderProgram;
    glm::vec3 color;
    Camera* camera;
    ShaderLoader* shaderLoader;
    PointLights* pointLights;
    SpotLight* spotLight;
    DirLight* dirLight;
    bool first;
    bool skybox;
public:
    Shader(const char *vertexShaderType, const char *fragmentShaderType);
    Shader();
    ~Shader();
    void useShader();
    void setModelViewProjectionMatrix(Transformation* transformation);
    void setCamera(Camera* camera);
    Camera* getCamera() {return this->camera;};
    void update(Subject* sender) override;
    void setUniformVar(const char* name, glm::mat4 matrix);
    void setUniformVar(const char* name, glm::vec3 vector);
    void setUniformVar(const char* name, float value);
    void setUniformVars(std::vector<PointLight*> pointLights);
    void setPointLights(PointLights* pointLights);
    void setSpotLight(SpotLight* spotLight);
    void setDirLight(DirLight* dirLight);
    void updateLights();
    void updateMaterial(Material* material);
    void toggleFlashlight(bool on);
    void setSkybox();
};
