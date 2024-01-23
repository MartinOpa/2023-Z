#ifndef LSPROGRAM_H
#define LSPROGRAM_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <string.h>
#include <time.h>

extern "C" {
    void runLs(int argc, char* argv[]);
}

#endif