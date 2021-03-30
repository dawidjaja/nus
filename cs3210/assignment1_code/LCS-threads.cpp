#include <algorithm>
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define N_THREADS 8

int M, N;
int width;
int *mat[3];

int prev2 = 0;
int prev = 1;
int cur = 2;

int count = 0;
int done = 0;

char *seq1;
char *seq2;

pthread_mutex_t barrier_lock = PTHREAD_MUTEX_INITIALIZER;

pthread_cond_t barrier_cond;

void* executor(void* threadid) {
    // Initialise two rows of the matrix with (M + 1) rows x (N + 1) columns
    long tid = (long) threadid;
    for (int d = 2; d <= M + N; d++) {
        //printf("-- %d %d --\n", d, threadid);
        for (int i = std::max(1, d - M) + tid; i <= std::min(d - 1, N); i += N_THREADS) {
            //printf("process %d %d\n", i, d);
            int seq1_idx = d - i - 1;
            int seq2_idx = i - 1;
            if (seq1[seq1_idx] == seq2[seq2_idx]) {
                mat[cur][i] = mat[prev2][i - 1] + 1;
            } else {
                int up = mat[prev][i];
                int left = mat[prev][i - 1];
                mat[cur][i] = std::max(up, left);
            }
            //printf("ans %d %d: %d\n", cur, i, mat[cur][i]);
        }
        //printf("--- %d %d done ---\n", d, threadid);

        pthread_mutex_lock(&barrier_lock);
        count++;
        if (count == N_THREADS) {
            if (d < width) {
                mat[cur][d] = 0;
            }
            int temp = prev2;
            prev2 = prev;
            prev = cur;
            cur = temp;
            count = 0;
            pthread_cond_broadcast(&barrier_cond);
        } else {
            pthread_cond_wait(&barrier_cond, &barrier_lock);
        }
        pthread_mutex_unlock(&barrier_lock);

    }
}

int main(int argc, char *argv[]) {
    // Accepts filepaths to two input DNA sequences as command-line arguments
    if (argc != 3) {
        printf("Usage: [executable] [file 1] [file 2]\n");
        return 1;
    }

    // Validate both filepaths
    FILE *file1 = fopen(argv[1], "r");
    FILE *file2 = fopen(argv[2], "r");
    if (!file1 || !file2) {
        printf("Input files are not found!\n");
        return 1;
    }

    fscanf(file1, "%d", &M);
    fscanf(file2, "%d", &N);

    // Read two input sequences
    seq1 = (char *)malloc(M + 1);
    seq2 = (char *)malloc(N + 1);
    fscanf(file1, "%s", seq1);
    fscanf(file2, "%s", seq2);
    fclose(file1);
    fclose(file2);

    // Initialise threads
    pthread_t threads[N_THREADS];
    long threadid[N_THREADS];

    width = N + 1;
    mat[0] = (int *)malloc(width * sizeof(int));
    mat[1] = (int *)malloc(width * sizeof(int));
    mat[2] = (int *)malloc(width * sizeof(int));
    mat[0][0] = 0;
    mat[1][0] = 0;
    mat[1][1] = 0;
    mat[2][0] = 0;

    for (long t = 0; t < N_THREADS; t++) {
        long tid = t;
        threadid[tid] = tid;
        printf("creating threads %d\n", tid);
        int rc = pthread_create(&(threads[tid]), NULL, executor, (void*)tid);
        if (rc) {
            printf("Error: Return code from pthread_create() is %d\n", rc);
            exit(-1);
        }
    }

    for (int i = 0; i < N_THREADS; i++) {
        pthread_join(threads[i], NULL);
    }

    printf("Final Answer: %d\n", mat[prev][N]);

    free(seq1);
    free(seq2);
    free(mat[0]);
    free(mat[1]);
    free(mat[2]);
}
