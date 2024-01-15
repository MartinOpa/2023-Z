#include "headers/SceneCreator.h"

SceneCreator::SceneCreator() {
    current = 0;

    Scene* scene1 = new Scene(glm::vec3(0.f, 0.f, 30.f));

    Shader* sharedShader = new Shader("../src/headers/shaders/PhongVertex.glsl",
                                      "../src/headers/shaders/PhongFragment.glsl");

    SpotLight* spotLight = new SpotLight(
            glm::vec3(0.5f, 0.5f, 0.5f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(.0f, .0f, .0f),
            1.0f,
            0.009f,
            0.0032f,
            glm::cos(glm::radians(12.5f)),
            glm::cos(glm::radians(17.5f))
            );

    PointLights* pointLights = new PointLights();
    PointLight* pointLight = new PointLight(
            glm::vec3(0.75f, 0.75f, 0.75f),
            glm::vec3(.5f, .5f, .5f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(1.0f, 1.f, 1.f),
            glm::vec3( 0.0f,  0.0f,  0.0f),
            1.0f,
            0.09f,
            0.032f
            );

    pointLights->add(pointLight);

    DirLight* dirLight = new DirLight(
            glm::vec3(0.2f, 0.2f, 0.2f),
            glm::vec3(0.4f, 0.4f, 0.4f),
            glm::vec3(0.5f, 0.5f, 0.5f),
            glm::vec3(.0f, .0f, .0f),
            glm::vec3(-0.2f, -1.0f, -0.3f)
            );

    sharedShader->setSpotLight(spotLight);
    sharedShader->setPointLights(pointLights);
    sharedShader->setDirLight(dirLight);

    Material* ballMaterial = new Material(glm::vec3(.2f, .2f, 1.f),
                                          glm::vec3(.75f, .75f, .75f),
                                          16.0f);

    CompositeTransformation* transformation = new CompositeTransformation();
    transformation->add(new Position(glm::vec3(.1f, 10.f, .1f)));
    transformation->add(new Scale(glm::vec3(4.f)));

    scene1->addModel(new DrawableObject(sphere,
                              ballMaterial,
                              sharedShader,
                              transformation));

    CompositeTransformation* transformation2 = new CompositeTransformation();
    transformation2->add(new Position(glm::vec3(.1f, -10.f, .1f)));
    transformation2->add(new Scale(glm::vec3(4.f)));

    scene1->addModel(new DrawableObject(sphere,
                              ballMaterial,
                              sharedShader,
                              transformation2));

    CompositeTransformation* transformation3 = new CompositeTransformation();
    transformation3->add(new Position(glm::vec3(10.f, .1f, .1f)));
    transformation3->add(new Scale(glm::vec3(4.f)));

    scene1->addModel(new DrawableObject(sphere,
                              ballMaterial,
                              sharedShader,
                              transformation3));

    CompositeTransformation* transformation4 = new CompositeTransformation();
    transformation4->add(new Position(glm::vec3(-10.f, .1f, .1f)));
    transformation4->add(new Scale(glm::vec3(4.f)));

    scene1->addModel(new DrawableObject(sphere,
                              ballMaterial,
                              sharedShader,
                              transformation4));

    this->scenes.push_back(scene1);

    Material* treeMaterial = new Material(glm::vec3(.1f, 1.f, .1f),
                                          glm::vec3(.1f, .1f, .1f),
                                          1.0f);

    Material* plainMaterial = new Material(glm::vec3(.1f, 1.f, .1f),
                                           glm::vec3(.25f, .25f, .25f),
                                           1.0f);

    Material* suziMaterial = new Material(glm::vec3(.8f, .8f, .8f),
                                          glm::vec3(.75f, .75f, .75f),
                                          32.0f);

    SpotLight* spotLightForest = new SpotLight(
            glm::vec3(0.5f, 0.5f, 0.5f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(0.f, 0.f, 0.f),
            1.0f,
            0.009f,
            0.0032f,
            glm::cos(glm::radians(15.f)),
            glm::cos(glm::radians(25.f))
    );

    PointLights* pointLightsForest = new PointLights();
    PointLight* forestLight = new PointLight(
            glm::vec3(0.25f, 0.25f, 0.25f),
            glm::vec3(0.8f, 0.8f, 0.8f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(1.0f, 0.1f, 0.1f),
            glm::vec3( 0.0f,  8.0f,  0.0f),
            1.0f,
            0.3f,
            0.1f
    );

    pointLightsForest->add(forestLight);

    DirLight* forestDirLight = new DirLight(
            glm::vec3(0.05f, 0.05f, 0.05f),
            glm::vec3(0.4f, 0.4f, 0.4f),
            glm::vec3(0.5f, 0.5f, 0.5f),
            glm::vec3(.25f, .25f, .25f),
            glm::vec3(-0.2f, -1.0f, -0.3f)
    );

    Shader* treeShader = new Shader("../src/headers/shaders/PhongVertex.glsl",
                                    "../src/headers/shaders/PhongFragment.glsl");

    treeShader->setSpotLight(spotLightForest);
    treeShader->setPointLights(pointLightsForest);
    treeShader->setDirLight(forestDirLight);

    Shader* plainScene2Shader = new Shader("../src/headers/shaders/PhongVertexTex.glsl",
                                        "../src/headers/shaders/PhongFragmentTex.glsl");

    plainScene2Shader->setSpotLight(spotLightForest);
    plainScene2Shader->setPointLights(pointLightsForest);
    plainScene2Shader->setDirLight(forestDirLight);

    Scene* scene2 = new Scene(glm::vec3(0.f, 0.f, 30.f));

    CompositeTransformation* plainTransformation = new CompositeTransformation();
    plainTransformation->add(new Position(glm::vec3(0.f, 0.f, 0.f)));
    plainTransformation->add(new Scale(glm::vec3(60.f)));

    scene2->addModel(new DrawableObject(plainTex,
                                        plainMaterial,
                                        "../src/headers/textures/grass.png",
                                        plainScene2Shader,
                                        plainTransformation));

    CompositeTransformation* treeTransformation = new CompositeTransformation();
    treeTransformation->add(new Position(glm::vec3(-50.f, 0.f, -50.f)));
    treeTransformation->add(new Rotation(glm::vec3(1.f, 0.f, 0.f), -0.85f));
    treeTransformation->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation));

    CompositeTransformation* treeTransformation1 = new CompositeTransformation();
    treeTransformation1->add(new Position(glm::vec3(-38.f, 0.f, -45.f)));
    treeTransformation1->add(new Scale(glm::vec3(7.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation1));

    CompositeTransformation* treeTransformation2 = new CompositeTransformation();
    treeTransformation2->add(new Position(glm::vec3(-25.f, 0.f, -50.f)));
    treeTransformation2->add(new Scale(glm::vec3(6.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation2));

    CompositeTransformation* treeTransformation3 = new CompositeTransformation();
    treeTransformation3->add(new Position(glm::vec3(-15.f, 0.f, -40.f)));
    treeTransformation3->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation3));

    CompositeTransformation* treeTransformation4 = new CompositeTransformation();
    treeTransformation4->add(new Position(glm::vec3(-5.f, 0.f, -35.f)));
    treeTransformation4->add(new Scale(glm::vec3(4.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation4));

    CompositeTransformation* treeTransformation5 = new CompositeTransformation();
    treeTransformation5->add(new Position(glm::vec3(10.f, 0.f, -40.f)));
    treeTransformation5->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation5));

    CompositeTransformation* treeTransformation6 = new CompositeTransformation();
    treeTransformation6->add(new Position(glm::vec3(35.f, 0.f, -40.f)));
    treeTransformation6->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation6));

    CompositeTransformation* treeTransformation7 = new CompositeTransformation();
    treeTransformation7->add(new Position(glm::vec3(50.f, 0.f, -37.f)));
    treeTransformation7->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation7));

    CompositeTransformation* treeTransformation8 = new CompositeTransformation();
    treeTransformation8->add(new Position(glm::vec3(-45.f, 0.f, -10.f)));
    treeTransformation8->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation8));

    CompositeTransformation* treeTransformation9 = new CompositeTransformation();
    treeTransformation9->add(new Position(glm::vec3(-35.f, 0.f, -15.f)));
    treeTransformation9->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation9));

    CompositeTransformation* treeTransformation10 = new CompositeTransformation();
    treeTransformation10->add(new Position(glm::vec3(-10.f, 0.f, -25.f)));
    treeTransformation10->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation10));

    CompositeTransformation* treeTransformation11 = new CompositeTransformation();
    treeTransformation11->add(new Position(glm::vec3(4.f, 0.f, -20.f)));
    treeTransformation11->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation11));

    CompositeTransformation* treeTransformation12 = new CompositeTransformation();
    treeTransformation12->add(new Position(glm::vec3(20.f, 0.f, -17.f)));
    treeTransformation12->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation12));

    CompositeTransformation* treeTransformation13 = new CompositeTransformation();
    treeTransformation13->add(new Position(glm::vec3(38.f, 0.f, -23.f)));
    treeTransformation13->add(new Scale(glm::vec3(4.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation13));

    CompositeTransformation* treeTransformation14 = new CompositeTransformation();
    treeTransformation14->add(new Position(glm::vec3(47.f, 0.f, -10.f)));
    treeTransformation14->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation14));

    CompositeTransformation* treeTransformation0 = new CompositeTransformation();
    treeTransformation0->add(new Position(glm::vec3(50.f, 0.f, 50.f)));
    treeTransformation0->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation0));

    CompositeTransformation* treeTransformation01 = new CompositeTransformation();
    treeTransformation01->add(new Position(glm::vec3(38.f, 0.f, 45.f)));
    treeTransformation01->add(new Scale(glm::vec3(7.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation01));

    CompositeTransformation* treeTransformation02 = new CompositeTransformation();
    treeTransformation02->add(new Position(glm::vec3(25.f, 0.f, 50.f)));
    treeTransformation02->add(new Scale(glm::vec3(6.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation02));

    CompositeTransformation* treeTransformation03 = new CompositeTransformation();
    treeTransformation03->add(new Position(glm::vec3(15.f, 0.f, 40.f)));
    treeTransformation03->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation03));

    CompositeTransformation* treeTransformation04 = new CompositeTransformation();
    treeTransformation04->add(new Position(glm::vec3(5.f, 0.f, 35.f)));
    treeTransformation04->add(new Scale(glm::vec3(4.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation04));

    CompositeTransformation* treeTransformation05 = new CompositeTransformation();
    treeTransformation05->add(new Position(glm::vec3(-10.f, 0.f, 40.f)));
    treeTransformation05->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation05));

    CompositeTransformation* treeTransformation06 = new CompositeTransformation();
    treeTransformation06->add(new Position(glm::vec3(-35.f, 0.f, 40.f)));
    treeTransformation06->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation06));

    CompositeTransformation* treeTransformation07 = new CompositeTransformation();
    treeTransformation07->add(new Position(glm::vec3(-50.f, 0.f, 37.f)));
    treeTransformation07->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation07));

    CompositeTransformation* treeTransformation08 = new CompositeTransformation();
    treeTransformation08->add(new Position(glm::vec3(45.f, 0.f, 10.f)));
    treeTransformation08->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation08));

    CompositeTransformation* treeTransformation09 = new CompositeTransformation();
    treeTransformation09->add(new Position(glm::vec3(35.f, 0.f, 15.f)));
    treeTransformation09->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation09));

    CompositeTransformation* treeTransformation010 = new CompositeTransformation();
    treeTransformation010->add(new Position(glm::vec3(10.f, 0.f, 25.f)));
    treeTransformation010->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation010));

    CompositeTransformation* treeTransformation011 = new CompositeTransformation();
    treeTransformation011->add(new Position(glm::vec3(-4.f, 0.f, 20.f)));
    treeTransformation011->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation011));

    CompositeTransformation* treeTransformation012 = new CompositeTransformation();
    treeTransformation012->add(new Position(glm::vec3(-20.f, -0.5f, 17.f)));
    treeTransformation012->add(new Rotation(glm::vec3(1.f, 0.f, 0.f), 1.2f));
    treeTransformation012->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation012));

    CompositeTransformation* treeTransformation013 = new CompositeTransformation();
    treeTransformation013->add(new Position(glm::vec3(-38.f, 0.f, 23.f)));
    treeTransformation013->add(new Scale(glm::vec3(4.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation013));

    CompositeTransformation* treeTransformation014 = new CompositeTransformation();
    treeTransformation014->add(new Position(glm::vec3(-47.f, 0.f, 10.f)));
    treeTransformation014->add(new Scale(glm::vec3(3.f)));

    scene2->addModel(new DrawableObject(tree,
                                        treeMaterial,
                               treeShader,
                               treeTransformation014));

    CompositeTransformation* bushesTransformation = new CompositeTransformation();
    bushesTransformation->add(new Position(glm::vec3(-37.5f, 0.f, -15.f)));
    bushesTransformation->add(new Scale(glm::vec3(4.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation));

    CompositeTransformation* bushesTransformation1 = new CompositeTransformation();
    bushesTransformation1->add(new Position(glm::vec3(-25.f, 0.f, -7.5f)));
    bushesTransformation1->add(new Scale(glm::vec3(8.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation1));

    CompositeTransformation* bushesTransformation2 = new CompositeTransformation();
    bushesTransformation2->add(new Position(glm::vec3(-19.f, 0.f, -6.f)));
    bushesTransformation2->add(new Scale(glm::vec3(9.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation2));

    CompositeTransformation* bushesTransformation3 = new CompositeTransformation();
    bushesTransformation3->add(new Position(glm::vec3(-1.f, 0.f, -16.5f)));
    bushesTransformation3->add(new Scale(glm::vec3(6.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation3));

    CompositeTransformation* bushesTransformation4 = new CompositeTransformation();
    bushesTransformation4->add(new Position(glm::vec3(7.f, 0.f, -23.f)));
    bushesTransformation4->add(new Scale(glm::vec3(7.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation4));

    CompositeTransformation* bushesTransformation5 = new CompositeTransformation();
    bushesTransformation5->add(new Position(glm::vec3(15.f, 0.f, -8.f)));
    bushesTransformation5->add(new Scale(glm::vec3(9.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation5));

    CompositeTransformation* bushesTransformation6 = new CompositeTransformation();
    bushesTransformation6->add(new Position(glm::vec3(21.5f, 0.f, -5.f)));
    bushesTransformation6->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation6));

    CompositeTransformation* bushesTransformation7 = new CompositeTransformation();
    bushesTransformation7->add(new Position(glm::vec3(26.5f, 0.f, -6.f)));
    bushesTransformation7->add(new Scale(glm::vec3(8.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation7));

    CompositeTransformation* bushesTransformation8 = new CompositeTransformation();
    bushesTransformation8->add(new Position(glm::vec3(31.5f, 0.f, -6.5f)));
    bushesTransformation8->add(new Scale(glm::vec3(7.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation8));

    CompositeTransformation* bushesTransformation9 = new CompositeTransformation();
    bushesTransformation9->add(new Position(glm::vec3(12.5f, 0.f, -13.f)));
    bushesTransformation9->add(new Scale(glm::vec3(12.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation9));

    CompositeTransformation* bushesTransformation10 = new CompositeTransformation();
    bushesTransformation10->add(new Position(glm::vec3(13.f, 0.f, -2.f)));
    bushesTransformation10->add(new Scale(glm::vec3(10.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation10));

    CompositeTransformation* bushesTransformation0 = new CompositeTransformation();
    bushesTransformation0->add(new Position(glm::vec3(7.5f, 0.f, 15.f)));
    bushesTransformation0->add(new Scale(glm::vec3(4.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation0));

    CompositeTransformation* bushesTransformation01 = new CompositeTransformation();
    bushesTransformation01->add(new Position(glm::vec3(5.f, 0.f, 17.f)));
    bushesTransformation01->add(new Scale(glm::vec3(8.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation01));

    CompositeTransformation* bushesTransformation02 = new CompositeTransformation();
    bushesTransformation02->add(new Position(glm::vec3(-30.f, 0.f, 6.f)));
    bushesTransformation02->add(new Scale(glm::vec3(8.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation02));

    CompositeTransformation* bushesTransformation03 = new CompositeTransformation();
    bushesTransformation03->add(new Position(glm::vec3(-10.f, 0.f, 6.5f)));
    bushesTransformation03->add(new Scale(glm::vec3(6.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation03));

    CompositeTransformation* bushesTransformation04 = new CompositeTransformation();
    bushesTransformation04->add(new Position(glm::vec3(-20.f, 0.f, 5.f)));
    bushesTransformation04->add(new Scale(glm::vec3(7.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation04));

    CompositeTransformation* bushesTransformation05 = new CompositeTransformation();
    bushesTransformation05->add(new Position(glm::vec3(-15.f, 0.f, 8.f)));
    bushesTransformation05->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation05));

    CompositeTransformation* bushesTransformation06 = new CompositeTransformation();
    bushesTransformation06->add(new Position(glm::vec3(-14.5f, 0.f, 12.f)));
    bushesTransformation06->add(new Scale(glm::vec3(9.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation06));

    CompositeTransformation* bushesTransformation07 = new CompositeTransformation();
    bushesTransformation07->add(new Position(glm::vec3(-6.5f, 0.f, 20.f)));
    bushesTransformation07->add(new Scale(glm::vec3(10.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation07));

    CompositeTransformation* bushesTransformation08 = new CompositeTransformation();
    bushesTransformation08->add(new Position(glm::vec3(-10.5f, 0.f, 6.5f)));
    bushesTransformation08->add(new Scale(glm::vec3(8.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation08));

    CompositeTransformation* bushesTransformation09 = new CompositeTransformation();
    bushesTransformation09->add(new Position(glm::vec3(-20.5f, 0.f, 26.f)));
    bushesTransformation09->add(new Scale(glm::vec3(4.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation09));

    CompositeTransformation* bushesTransformation010 = new CompositeTransformation();
    bushesTransformation010->add(new Position(glm::vec3(-15.f, 0.f, 2.f)));
    bushesTransformation010->add(new Scale(glm::vec3(6.f)));

    scene2->addModel(new DrawableObject(bushes,
                                        treeMaterial,
                               treeShader,
                               bushesTransformation010));

    CompositeTransformation* suziSmoothTransformation = new CompositeTransformation();
    suziSmoothTransformation->add(new Position(glm::vec3(0.f, 1.f, 0.f)));
    suziSmoothTransformation->add(new Rotation(glm::vec3(1.f, 0.f, 0.f), -0.60f));
    suziSmoothTransformation->add(new Rotation(glm::vec3(0.f, 1.f, 0.f), .35f));
    suziSmoothTransformation->add(new Scale(glm::vec3(5.f)));

    scene2->addModel(new DrawableObject(suziSmooth,
                                suziMaterial,
                                treeShader,
                                suziSmoothTransformation));

    scene2->setSkybox(new Skybox(new Shader("../src/headers/shaders/SkyboxVertex.glsl",
                                            "../src/headers/shaders/SkyboxFragment.glsl"),
                                 "../src/headers/textures/night/",
                                 skybox));

    this->scenes.push_back(scene2);

    Scene* scene3 = new Scene(glm::vec3(0.f, 0.f, 30.f));

    Shader* texSceneShader = new Shader("../src/headers/shaders/PhongVertexTex.glsl",
                                      "../src/headers/shaders/PhongFragmentTex.glsl");

    SpotLight* spotLightTex = new SpotLight(
            glm::vec3(0.5f, 0.5f, 0.5f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(.0f, .0f, .0f),
            1.0f,
            0.009f,
            0.0032f,
            glm::cos(glm::radians(12.5f)),
            glm::cos(glm::radians(17.5f))
    );

    PointLights* pointLightsTex = new PointLights();
    PointLight* pointLightTex = new PointLight(
            glm::vec3(0.75f, 0.75f, 0.75f),
            glm::vec3(.5f, .5f, .5f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(.0f, 0.f, 0.f),
            glm::vec3( 0.0f,  0.0f,  0.0f),
            1.0f,
            0.09f,
            0.032f
    );

    pointLightsTex->add(pointLightTex);

    DirLight* dirLightTex = new DirLight(
            glm::vec3(0.5f, 0.5f, 0.5f),
            glm::vec3(0.3f, 0.3f, 0.3f),
            glm::vec3(0.5f, 0.5f, 0.5f),
            glm::vec3(1.0f, 1.0f, 1.0f),
            glm::vec3(-0.2f, -1.0f, -0.3f)
    );

    texSceneShader->setSpotLight(spotLightTex);
    texSceneShader->setPointLights(pointLightsTex);
    texSceneShader->setDirLight(dirLightTex);

    Material* texPlainMaterial = new Material(glm::vec3(.1f, 1.f, .1f),
                                              glm::vec3(.25f, .25f, .25f),
                                              1.0f);

    CompositeTransformation* transformationTex = new CompositeTransformation();
    transformationTex->add(new Position(glm::vec3(0.f, 0.f, 0.f)));
    transformationTex->add(new Scale(glm::vec3(16.f)));

    scene3->addModel(new DrawableObject(plainTex,
                                        texPlainMaterial,
                                        "../src/headers/textures/grass.png",
                                        texSceneShader,
                                        transformationTex));

    scene3->setSkybox(new Skybox(new Shader("../src/headers/shaders/SkyboxVertex.glsl",
                                            "../src/headers/shaders/SkyboxFragment.glsl"),
                                 "../src/headers/textures/day/",
                                 skybox));

    CompositeTransformation* transformationAssimp = new CompositeTransformation();
    transformationAssimp->add(new Position(glm::vec3(0.f, 0.f, 0.f)));
    transformationAssimp->add(new Scale(glm::vec3(1.f)));

    Shader* texSceneShaderHouse = new Shader("../src/headers/shaders/PhongVertexTex.glsl",
                                        "../src/headers/shaders/PhongFragmentTex.glsl");

    texSceneShaderHouse->setSpotLight(spotLightTex);
    texSceneShaderHouse->setPointLights(pointLightsTex);
    texSceneShaderHouse->setDirLight(dirLightTex);

    scene3->addModel(new DrawableObject("../src/headers/textures/model.obj",
                                        "../src/headers/textures/model.png",
                                        texPlainMaterial,
                                        texSceneShaderHouse,
                                        transformationAssimp));

    CompositeTransformation* transformationAssimpZombie = new CompositeTransformation();
    transformationAssimpZombie->add(new Position(glm::vec3(0.5f, 0.f, 11.5f)));
    transformationAssimpZombie->add(new Scale(glm::vec3(1.f)));
    transformationAssimpZombie->add(new Rotation(glm::vec3(0.f, 1.f, 0.f), 3.4f));

    scene3->addModel(new DrawableObject("../src/headers/textures/zombie.obj",
                                        "../src/headers/textures/zombie.png",
                                        texPlainMaterial,
                                        texSceneShaderHouse,
                                        transformationAssimpZombie));

    CompositeTransformation* transformationAssimpZombie1 = new CompositeTransformation();
    transformationAssimpZombie1->add(new Position(glm::vec3(-2.f, 0.f, 13.f)));
    transformationAssimpZombie1->add(new Scale(glm::vec3(1.1f)));
    transformationAssimpZombie1->add(new Rotation(glm::vec3(0.f, 1.f, 0.f), 2.3f));

    scene3->addModel(new DrawableObject("../src/headers/textures/zombie.obj",
                                        "../src/headers/textures/zombie.png",
                                        texPlainMaterial,
                                        texSceneShaderHouse,
                                        transformationAssimpZombie1));

    CompositeTransformation* transformationAssimpZombie2 = new CompositeTransformation();
    transformationAssimpZombie2->add(new Position(glm::vec3(4.f, 0.f, 11.5f)));
    transformationAssimpZombie2->add(new Scale(glm::vec3(1.f)));
    transformationAssimpZombie2->add(new Rotation(glm::vec3(0.f, 1.f, 0.f), 5.2f));

    scene3->addModel(new DrawableObject("../src/headers/textures/zombie.obj",
                                        "../src/headers/textures/zombie.png",
                                        texPlainMaterial,
                                        texSceneShaderHouse,
                                        transformationAssimpZombie2));

    CompositeTransformation* transformationAssimpZombie3 = new CompositeTransformation();
    transformationAssimpZombie3->add(new Position(glm::vec3(0.f, 0.1f, 10.f)));
    transformationAssimpZombie3->add(new Scale(glm::vec3(1.f)));
    transformationAssimpZombie3->add(new Rotation(glm::vec3(0.f, 1.f, 0.f), 3.2f));

    scene3->addModel(new DrawableObject("../src/headers/textures/zombie.obj",
                                        "../src/headers/textures/zombie.png",
                                        texPlainMaterial,
                                        texSceneShaderHouse,
                                        transformationAssimpZombie3));

    this->scenes.push_back(scene3);
}

Scene* SceneCreator::getNext() {
    current++;
    if (current >= this->scenes.size()) {
        current = 0;
    }
    return this->scenes[current];
}

Scene* SceneCreator::getFirst() {
    return this->scenes[0];
}

SceneCreator::~SceneCreator() {

}