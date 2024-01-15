#pragma once

#include "IncludeLibs.h"

class Subject;
class Observer {
public:
    virtual void update(Subject* sender) = 0;
};