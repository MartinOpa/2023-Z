#include "matgen.h"
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

const char* getOperand(int num) {
    switch(num) {
        case 0:
            return "+";
            break;
        case 1:
            return "-";
            break;
        case 2:
            return "*";
            break;
        default:
            return "/";
            break;
    }
}

const char* getBinaryOperand(int num) {
    switch(num) {
        case 0:
            return "|";
            break;
        case 1:
            return "&";
            break;
        default:
            return "^";
            break;
    }
}

int matgen(int argc, char* argv[]) {
    time_t t;

    srand((unsigned) time(&t));

    int count = -1;
    int i = 0;
    
    if (argc > 0) {
        count = atoi(argv[1]);
    } 
    
    bool bin = false;
    for (int i = 1; i < argc; i++) {
        if (strcmp(argv[i], "-l") == 0) {
                bin = true;
        }
    }

    if (bin) {
        while(count < 0 || i < count) {
            printf("%d%s%d\n", rand()%2, getBinaryOperand(rand()%3), rand()%2);
            i++;
        }
    } else {
        while(count < 0 || i < count) {
            printf("%d%s%d\n", rand()%1000, getOperand(rand()%4), rand()%1000);
            i++;
        }
    }
    
    return 0;
}