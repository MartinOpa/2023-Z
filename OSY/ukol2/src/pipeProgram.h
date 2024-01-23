#ifndef PIPEPROGRAM_H
#define PIPEPROGRAM_H

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <sys/wait.h>
#include <string.h>

extern "C" {
    void runPipe();
}

#endif