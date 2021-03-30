#include <signal.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include "flag.h"                       /* char* flag = "flag{...}"; // Interact with the server to get flag! */

static const int correctAnswer = 0xABCD2107;

// Exit program after 5s
void timeout() {
  printf("\nToo slow!\n");
  _exit(1);
}

int main(int argc, char **argv)
{	
	setvbuf(stdin,  NULL, _IONBF, 0);   // Switch off I/O buffering
	setvbuf(stdout, NULL, _IONBF, 0);   // Switch off I/O buffering
	setvbuf(stderr, NULL, _IONBF, 0);   // Switch off I/O buffering

	signal(SIGALRM, timeout);           // Set program to exit on timeout
	alarm(5);                           // Exit program after 5s

	int x = 0;
	char buffer[36];

	printf("What do you want to put in buffer? ");
	fflush(stdout); 
	gets(buffer);

	printf("\nExpected value of x: %x\n", correctAnswer);
	printf("Actual value of x: %x\n", x);
	fflush(stdout);

	if(x == correctAnswer)
	{
		// winner!
		printf("\nYou win! Flag: %s\n", flag);
		fflush(stdout);
	}
	else
	{
		// loser!
		printf("Try again!\n");
		fflush(stdout);
		return 1;
	}

	return 0;
}
