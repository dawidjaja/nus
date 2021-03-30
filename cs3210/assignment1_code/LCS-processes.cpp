#include <errno.h>
#include <fcntl.h>
#include <semaphore.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/shm.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

#include <algorithm>
#include <iostream>
#include <vector>

#define N_PROCS 8

int main(int argc, char *argv[]) {
    // Accepts filepaths to two input DNA sequences as command-line arguments
    if (argc != 3) {
        std::cout << "Usage: [executable] [file 1] [file 2]" << std::endl;
        return 1;
    }

    // Validate both filepaths
    FILE *file1 = fopen(argv[1], "r");
    FILE *file2 = fopen(argv[2], "r");
    if (!file1 || !file2) {
        std::cout << "Input files are not found!" << std::endl;
        return 1;
    }

    int M, N;
    fscanf(file1, "%d", &M);
    fscanf(file2, "%d", &N);

    // Read two input sequences
    char *seq1 = (char *)malloc(M + 1);
    char *seq2 = (char *)malloc(N + 1);
    fscanf(file1, "%s", seq1);
    fscanf(file2, "%s", seq2);
    fclose(file1);
    fclose(file2);

    /**
     * Initialize matrix
     */
    int width = N + 1;
    int *mat[3];

    int shmid_0 = shmget(0, width * sizeof(int), 0644 | IPC_CREAT);
    if (shmid_0 < 0) { /* shared memory error check */
        std::cerr << "shmget" << std::endl;
        exit(1);
    }
    mat[0] = (int *)shmat(shmid_0, NULL, 0);

    int shmid_1 = shmget(1, width * sizeof(int), 0644 | IPC_CREAT);
    if (shmid_1 < 0) { /* shared memory error check */
        std::cerr << "shmget" << std::endl;
        exit(1);
    }
    mat[1] = (int *)shmat(shmid_1, NULL, 0);

    int shmid_2 = shmget(2, width * sizeof(int), 0644 | IPC_CREAT);
    if (shmid_2 < 0) { /* shared memory error check */
        std::cerr << "shmget" << std::endl;
        exit(1);
    }
    mat[2] = (int *)shmat(shmid_2, NULL, 0);

    mat[1][0] = 0;
    mat[2][0] = 0;
    for (int i = 0; i < width; i++) {
        mat[i % 3][i] = 0;
    }

    int prev2 = 0;
    int prev = 1;
    int cur = 2;
    int temp;

    /**
     * Initialize semaphores
     */
    sem_t *barriers[2];
    barriers[0] = sem_open("barrier_0", O_CREAT | O_EXCL, 0644, 0);
    barriers[1] = sem_open("barrier_1", O_CREAT | O_EXCL, 0644, 0);
    sem_t *count_mutex = sem_open("mutex", O_CREAT | O_EXCL, 0644, 1);
    int *count;
    int shmid_3 = shmget(3, sizeof(int), 0644 | IPC_CREAT);
    if (shmid_3 < 0) { /* shared memory error check */
        std::cerr << "shmget" << std::endl;
        exit(1);
    }
    count = (int *)shmat(shmid_3, NULL, 0);
    *count = 0;

    // Assign a worker_id per process: 0 for parent, 1..N_PROCS-1 for children
    int worker_id = 0;

    for (int id = 1; id < N_PROCS; id++) {
        pid_t pid = fork();
        if (pid < 0) {
            // TODO: cleanup semaphore
            std::cerr << "Fork error." << std::endl;
        } else if (pid == 0) {
            worker_id = id;
            break;
        }
    }

    for (int d = 2; d <= M + N; d++) {
        for (int i = std::max(1, d - M) + worker_id; i <= std::min(d - 1, N);
             i += N_PROCS) {
            int seq1_idx = d - i - 1;
            int seq2_idx = i - 1;
            if (seq1[seq1_idx] == seq2[seq2_idx]) {
                mat[cur][i] = mat[prev2][i - 1] + 1;
            } else {
                int up = mat[prev][i];
                int left = mat[prev][i - 1];
                mat[cur][i] = std::max(up, left);
            }
        }
        temp = prev2;
        prev2 = prev;
        prev = cur;
        cur = temp;

        // Synchronize
        sem_wait(count_mutex);
        (*count)++;
        if (*count == N_PROCS) {
            for (int i = 0; i < N_PROCS; i++) {
                sem_post(barriers[d % 2]);
            }
            *count = 0;
        }
        sem_post(count_mutex);

        sem_wait(barriers[d % 2]);
    }

    if (worker_id != 0) {
        exit(0);
    }

    std::cout << mat[prev][N];

    free(seq1);
    free(seq2);
    shmdt(mat[0]);
    shmctl(shmid_0, IPC_RMID, 0);
    shmdt(mat[1]);
    shmctl(shmid_1, IPC_RMID, 0);
    shmdt(mat[2]);
    shmctl(shmid_2, IPC_RMID, 0);
    shmdt(count);
    shmctl(shmid_3, IPC_RMID, 0);

    sem_unlink("barrier_0");
    sem_close(barriers[0]);
    sem_unlink("barrier_1");
    sem_close(barriers[1]);
    sem_unlink("mutex");
    sem_close(count_mutex);

    while (wait(NULL) > 0) {
    }
}
