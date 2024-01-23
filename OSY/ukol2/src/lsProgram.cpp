#include "lsProgram.h"

void process(char* filename, int size, int mode, int time) {
    struct stat l_mystat;

    if (stat(filename, &l_mystat) == -1) {
        printf("Soubor '%s' nenalezen.\n", filename);
        return;
    }

    // if ((l_mystat.st_mode & S_IRGRP) || (l_mystat.st_mode & S_IWGRP) || (l_mystat.st_mode & S_IROTH) || (l_mystat.st_mode & S_IWOTH)) {
    //     printf("Soubor '%s' smí číst i skupina nebo ostatní uživatelé.\n", filename);
    //     return;
    // } POTOM ODKOMENTUJ

    if (mode) {
        printf((l_mystat.st_mode & S_IRUSR) ? "r" : "-");
        printf((l_mystat.st_mode & S_IWUSR) ? "w" : "-");
        printf((l_mystat.st_mode & S_IXUSR) ? "x" : "-");
        printf((l_mystat.st_mode & S_IRGRP) ? "r" : "-");
        printf((l_mystat.st_mode & S_IWGRP) ? "w" : "-");
        printf((l_mystat.st_mode & S_IXGRP) ? "x" : "-");
        printf((l_mystat.st_mode & S_IROTH) ? "r" : "-");
        printf((l_mystat.st_mode & S_IWOTH) ? "w" : "-");
        printf((l_mystat.st_mode & S_IXOTH) ? "x" : "-");
    }

    if (size) {
        printf("    %d", (int)l_mystat.st_size);
    }

    if (time) { 
        char timeStr[32];
        strftime(timeStr, sizeof(timeStr), "%Y-%m-%d %H:%M", localtime(&l_mystat.st_mtime));
        printf("    %s", timeStr);
    }

    printf("    %s", filename);

    printf("\n");

    if (fork() == 0) {
        char* lFileName = filename;
        int lastSize = (int)l_mystat.st_size;
        struct stat l_myCurrentStat;

        while (1) {
            stat(lFileName, &l_myCurrentStat);
            int currentSize = (int)l_myCurrentStat.st_size;
            if (currentSize > lastSize) {
                lastSize = currentSize;

                FILE *file = fopen(lFileName, "r");
                char *lastLine = NULL;
                char *currentLine = NULL;
                size_t len = 0;
                ssize_t read;

                while (getline(&currentLine, &len, file) != -1) {
                    lastLine = strdup(currentLine);
                }

                if (lastLine != NULL) {
                    printf("%s\n", lastLine);
                }

                fclose(file);
            }
            sleep(1);
        }
    } else {
        return;
    }
}

void runLs(int argc, char* argv[]) {
    int size = 0;
    int mode = 0;
    int time = 0;

    if (argc < 2) {
        return;
    }

    for (int i = 1; i < argc; i++) {
        if (strcmp(argv[i], "-s") == 0) {
            size = 1;
        } else if (strcmp(argv[i], "-m") == 0) {
            mode = 1;
        } else if (strcmp(argv[i], "-t") == 0) {
            time = 1;
        } else {
            process(argv[i], size, mode, time);
        }
    }

    return;
}

int main(int argc, char* argv[]) {

    runLs(argc, argv); //př. ./lsProgram -s -t -m <soubor>

    return 0;
}

