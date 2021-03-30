#include <algorithm>
#include <stdio.h>
#include <stdlib.h>

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

    // Initialise two rows of the matrix with (M + 1) rows x (N + 1) columns
    int width = N + 1;
    int *mat[3];
    mat[0] = (int *)malloc(width * sizeof(int));
    mat[1] = (int *)malloc(width * sizeof(int));
    mat[2] = (int *)malloc(width * sizeof(int));
    mat[1][0] = 0;
    mat[2][0] = 0;
    for (int i = 0; i < width; i++) {
        mat[i % 3][i] = 0;
    }

    int prev2 = 0;
    int prev = 1;
    int cur = 2;
    int temp;
    for (int d = 2; d <= M + N; d++) {
        for (int i = std::max(1, d - M); i <= std::min(d - 1, N); i++) {
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
    }

    printf("%d", mat[prev][N]);

    free(seq1);
    free(seq2);
    free(mat[0]);
    free(mat[1]);
    free(mat[2]);
}
