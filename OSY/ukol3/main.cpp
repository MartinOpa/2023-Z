#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <errno.h>
#include <string.h>
#include <signal.h>
#include <stdarg.h>
#include <ctype.h>
#include <sys/types.h>
#include <sys/wait.h>

void execCommand(char* command, FILE* input, FILE* output) {
    int childPid = fork();
    
    if (childPid == -1) {
        exit(1);
    } 
    
    if (childPid == 0) {
        if (input != NULL) {
            dup2(fileno(input), STDIN_FILENO);
            fclose(input);
        }
        if (output != NULL) {
            dup2(fileno(output), STDOUT_FILENO);
            fclose(output);
        }

        char* args[1024];
        int i = 0;
        char* tok = strtok(command, " ");
        while (tok != NULL) {
            args[i] = tok;
            i++;
            tok = strtok(NULL, " ");
        }
        args[i] = NULL;

        execvp(args[0], args);
        exit(1);
    } else {
        int l_status;
        waitpid(childPid, &l_status, 0);
    }
}

int main() {
    while (1) {
        printf("$ ");

        char input[1024];
        if (fgets(input, sizeof(input), stdin) == NULL) {
            break;
        }

        if (input[strlen(input) - 1] == '\n') {
            input[strlen(input) - 1] = '\0';
        }

        char* commands[1024];
        int n = 0;
        FILE* outputFile = NULL;

        char* fileName = NULL;
        for (int i = 0; input[i] != '\0'; i++) {
            if (input[i] == '>') {
                fileName = &input[i + 2];
                outputFile = fopen(fileName, "w");
                input[i] = '\0';
                break;
            }
        }

        char* tok = strtok(input, "|");
        while (tok != NULL) {
            commands[n] = tok;
            n++;
            tok = strtok(NULL, "|");
        }

        if (n > 0) {
            FILE* inputFile = NULL;
            for (int i = 0; i < n-1; i++) {
                int myPipe[2];
                pipe(myPipe);
                execCommand(commands[i], inputFile, fdopen(myPipe[1], "w"));
                close(myPipe[1]);
                inputFile = fdopen(myPipe[0], "r");
            }
            execCommand(commands[n-1], inputFile, outputFile);
        } else {
            exit(0);
        }
    }

    return 0;
}
