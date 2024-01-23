#include "mateval.h"
#include <stdio.h>

void evaluate(char* radek) {
    int num1, num2, result;
    char op;

    while (scanf("%d %c %d", &num1, &op, &num2) == 3) {
        switch (op) {
            case '+':
                result = num1 + num2;
                break;
            case '-':
                result = num1 - num2;
                break;
            case '*':
                result = num1 * num2;
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    continue;
                }
                break;
            case '^':
                if (((num1 == 1) && (num2 == 0)) || ((num2 == 1) && (num1 == 0))) {
                    printf("true\n");
                } else {
                    printf("false\n");
                }
                continue;
                break;
            case '&':
                if ((num1 == 1) && (num2 == 1)) {
                    printf("true\n");
                } else {
                    printf("false\n");
                }
                continue;
                break;
            case '|':
                if ((num1 == 1) || (num2 == 1)) {
                    printf("true\n");
                } else {
                    printf("false\n");
                }
                continue;
                break;
            
        }

        printf("%d\n", result);
    }

}