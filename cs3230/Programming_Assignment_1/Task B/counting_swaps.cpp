#ifdef _WIN32
#define getchar_unlocked getchar
#endif
// DO NOT REMOVE THE CODE ABOVE
#include <bits/stdc++.h>

using namespace std;

// fast way to read next integer
inline int getint(){
	char c;
	while ((c = getchar_unlocked()) <= ' ');
	int v = 0;
	do { v = (v << 3) + (v << 1) + (c ^ '0'); }
	while ((c = getchar_unlocked()) >= '0' && c <= '9');
	return v;
}

// count number of swaps using divide and conquer
long long count_swaps(int L, int R, std::vector<int>& W, std::vector<int>& temp, int D) {
    // if only one element, no swaps needed
    if (L == R) return 0;

    // split array into left and right subarray
    // recurse into each subarray
    int M = (L + R) / 2;
    long long swaps = 0;
    swaps += count_swaps(L, M, W, temp, D);
    swaps += count_swaps(M + 1, R, W, temp, D);

    // sort by choosing the minimum of both subarrays
    int index1 = L, index2 = M + 1, index3 = 0;
    int ptr = L;
    while (index1 <= M && index2 <= R) {
        if (W[index1] <= W[index2]) {
            temp[index3++] = W[index1++];
        } else {
            // add number of swaps equal to number of elements remaining in left subarray
            // Hint: you need to modify this part to solve both parts of your assignment
            while (W[ptr] - W[index2] <= D && ptr <= M) {
                ptr++;
            }
            swaps += M - ptr + 1;
            temp[index3++] = W[index2++];
        }
    }

    // add any remaining elements in left subarray
    while (index1 <= M)
        temp[index3++] = W[index1++];

    // add any remaining elements in right subarray
    while (index2 <= R)
        temp[index3++] = W[index2++];

    // transfer elements back into original array
    memcpy(W.data() + L, temp.data(), index3 * sizeof(int));

    return swaps;
}

int main(){
    int N = getint();
    int D = getint();
    std::vector<int> W(N), temp(N);
    for (int i = 0; i < N; ++i) {
        W[i] = getint();
    }
    printf("%lld\n", count_swaps(0, N - 1, W, temp, D));
	return 0;
}
