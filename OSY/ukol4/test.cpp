#include <iostream>
#include <cstring> // for strlen
#include <string>

int main() {
    char buf[256]; // Replace this with your char array

    // Assume buf is filled with some data
    strcpy(buf, "Hello, world!");

    // Convert char array to std::string
    std::string myString(buf);

    // Now, myString contains the content of buf as a string
    std::cout << "String from char array: " << myString << std::endl;

    return 0;
}
