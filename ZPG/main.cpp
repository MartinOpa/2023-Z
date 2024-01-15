/**
 * @file main.cpp
 *
 * @brief Main function
 *
 * @author Martin Opalka
 **/

#include "src/headers/Application.h"

int main(void) {
    Application::getInstance().initialization();
    Application::getInstance().createModels();
    Application::getInstance().run();
}
