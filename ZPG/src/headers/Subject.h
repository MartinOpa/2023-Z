#pragma once

#include "IncludeLibs.h"

#include "Observer.h"

#include <vector>
#include <string>

class Subject {
private:
    std::vector<Observer*> observers;
public:
    void subscribe(Observer* observer);
    void unsubscribe(Observer* observer);
    void notifyObservers(std::string eventType, Subject* sender);
};
