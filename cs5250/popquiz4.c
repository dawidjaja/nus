#include <signal.h>
#include <stdio.h>
#include <stdlib.h>

void sigint_handler(int);
void sigusr1_handler(int);

int main() {
    signal(SIGINT, sigint_handler);
    signal(SIGUSR1, sigusr1_handler);

    while (1) {
        
    }
    return 0;
}

void sigint_handler(int signum) {
    printf("Sorry, I’m staying… not going anywhere.\n");
}

void sigusr1_handler(int signum) {
    printf("Alright, since you really want me dead. Goodbye, cruel world!\n");
    exit(1);
}
