#include "headers/Subject.h"

void Subject::subscribe(Observer* observer) {
    this->observers.push_back(observer);
}

void Subject::unsubscribe(Observer* observer) {
    //this->observers.erase(std::remove(this->observers.begin(), this->observers.end(), observer), this->observers.end());
}

void Subject::notifyObservers(std::string eventType, Subject* sender) {
    for (auto observer : observers) {
        observer->update(sender);
    }
}
