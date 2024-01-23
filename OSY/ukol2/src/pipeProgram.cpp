#include "pipeProgram.h"

int sleepTime = 1;

char getRandChar() {
    switch (rand() % 5) {
    case 0:
        return '+';
    case 1:
        return '-';
    case 2:
        return '*';
    case 3:
        return '/';
    default:
        return '%';
    }
}

int calc(int num1, char op, int num2) {
    switch (op) {
    case '+':
        return num1 + num2;
    case '-':
        return num1 - num2;
    case '*':
        return num1 * num2;
    case '/':
        return num1 / num2;
    default:
        return num1 % num2;
    }
}

void runPipe() {
    int myPipe[2];
    pipe(myPipe);
    
    if (fork() == 0) {
        close(myPipe[0]);
        while(1) { 
            int num = rand() % 1000;
            int num2 = rand() % 1000;
            char ch = getRandChar();
            char buf[128];

            sprintf(buf, "%d%c%d\n", num, ch, num2);
            write(myPipe[1], buf, strlen(buf));

            memset(buf, 0, sizeof(buf));

            sleep(sleepTime);
        }
        close(myPipe[1]);
        exit(0);
    } else { 
        int myPipeTwo[2];
        pipe(myPipeTwo);

        if (fork() == 0) {
            close(myPipe[1]);
            close(myPipeTwo[0]);
            while(1) {
                char bufTwo[128];
                char exp[128];
                int num1;
                char op;
                int num2;

                if (read(myPipe[0], exp, sizeof(exp)) == -1) {
                    memset(bufTwo, 0, sizeof(bufTwo));
                    sleep(sleepTime);
                    continue;
                }

                if (sscanf(exp, "%d%c%d", &num1, &op, &num2) == 3) {
                    sprintf(bufTwo, "%d%c%d=%d", num1, op, num2, calc(num1, op, num2));
                    write(myPipeTwo[1], bufTwo, strlen(bufTwo));
                } 

                memset(bufTwo, 0, sizeof(bufTwo));

                sleep(sleepTime);
            }
            close(myPipe[0]);
            close(myPipeTwo[1]);
            exit(0);
        } else { 
            close(myPipeTwo[1]);
            while(1) {
                char calcExp[128];

                if (read(myPipeTwo[0], calcExp, sizeof(calcExp)) == -1) {
                    memset(calcExp, 0, sizeof(calcExp));
                    sleep(sleepTime);
                    continue;
                }

                printf("%s\n", calcExp);

                memset(calcExp, 0, sizeof(calcExp));

                sleep(sleepTime);
            }    
            close(myPipeTwo[0]);
            exit(0);  
        }       
    }
    
    return;
}

int main(int argc, char* argv[]) {

    runPipe();

    return 0;
}

