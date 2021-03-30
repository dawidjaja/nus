#include <stdio.h>
#include <stdlib.h>
#include <string.h>

struct matchbook {
    unsigned char buffer[48];
    void (*release)();
};

void light(struct matchbook * mb, unsigned int number) {
    printf("otto %u\n", number);
    for (int i = 0; i < number; i++) {
        printf("How many dancing lights will she see? ");
        unsigned int how_many;
        scanf("%d", &how_many);
        if (how_many == 0) {
            break;
        }
        mb->buffer[i] = how_many & 0x000000ff;
    }
}

__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");
__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");
__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");__asm__("nop");

void dancing_visions() {
    puts("Her eyes are still open wide.");
    puts("The image fills all with dread.");
    puts("The last of her matches did die.");
    system("cat flag.txt");
}

void fade_away() {
    puts("People gather around the alley dark.");
    puts("As the ancient carolers sing.");
    puts("There are cries heard around the park.");
}

int main() {
    setvbuf(stdin, NULL, _IONBF, 0);
    setvbuf(stdout, NULL, _IONBF, 0);
    setvbuf(stderr, NULL, _IONBF, 0);

    struct matchbook * little_matchbook = malloc(sizeof(struct matchbook));
    memset(little_matchbook->buffer, 0, 48);
    little_matchbook->release = &fade_away;

    puts("Swaying on thin swaying feet.");
    puts("Cold winter frost choking her.");
    puts("A little girl makes her way down the street.\n");

    int number;
    puts("Precious matches number fourty-eight");
    printf("How many will you answer for: ");
    scanf("%d", &number);
    printf("%d\n", number);

    if (number > 48) {
        puts("You ask for the impossible.\n");
        number = 0;
    }
    else {
        light(little_matchbook, number);
    }

    for (int i = 0; i < number; i++) {
        printf("%d. \t", i + 1);
        for (int j = 0; j < little_matchbook->buffer[i]; j++) {
            printf("*");
        }
        puts("");
    }
    puts("");

    little_matchbook->release();
    free(little_matchbook);
}

