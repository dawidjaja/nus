#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<int,int>

using namespace std;

vector<int> v;
map<int,int> compress;
int l[100005], r[100005], n, bit[100005];

//This should be N log^2 N. but I think maybe the constant time too big. 

inline int getint(){
	char c;
	while ((c = getchar_unlocked()) <= ' ');
	int v = 0;
	do { v = (v << 3) + (v << 1) + (c ^ '0'); }
	while ((c = getchar_unlocked()) >= '0' && c <= '9');
	return v;
}

// count number of swaps using divide and conquer
long long count_swaps(int L, int R, vector<pii>& id, vector<pii>& temp) {
    // if only one element, no swaps needed
    if (L == R) return 0;

    // split array into left and right subarray
    // recurse into each subarray
    int M = (L + R) >> 1;
    long long swaps = 0;
    swaps += count_swaps(L, M, id, temp);
    swaps += count_swaps(M + 1, R, id, temp);

    // sort by choosing the minimum of both subarrays
    int index1 = L, index2 = M + 1, index3 = 0;
    int ptr = L; //fenwick tree to count how many smaller
    for (int i = 0; i < n; i++) {
        bit[i] = 0;
    }
    for (int i = L; i <= M; i++) {
        for (int j = compress[id[i].fs]; j < n; j += (j & -j)) { 
            bit[j]++;
        }
    }

    /*
    for (int i = L; i <= R; i++) {
        printf("%d -> %d %d\n", i, id[i].fs, id[i].sc);
    }
    */
    while (index1 <= M && index2 <= R) {
        if (id[index1].sc <= id[index2].sc) {
            for (int i = compress[id[index1].fs]; i < n; i += (i & -i)) {
                bit[i]--;
            }
            temp[index3++] = id[index1++];
        } else {
            // add number of swaps equal to number of elements remaining in left subarray
            // Hint: you need to modify this part to solve both parts of your assignment
            int idx = compress[id[index2].fs];
            //printf("idx - %d\n", idx);
            for (int i = r[idx]; i > 0; i -= (i & -i)) {
                swaps -= bit[i];
            }
            for (int i = l[idx] - 1; i > 0; i -= (i & -i)) {
                swaps += bit[i];
            }
            //printf("%lld - %d %d \n", x, M, index1);
            swaps += M + 1 - index1;
            temp[index3++] = id[index2++];
        }
    }
    //printf("%d - %d swaps %lld\n", L, R, swaps);

    // add any remaining elements in left subarray
    while (index1 <= M)
        temp[index3++] = id[index1++];

    // add any remaining elements in right subarray
    while (index2 <= R)
        temp[index3++] = id[index2++];

    // transfer elements back into original array
    //memcpy(id.data() + L, temp.data(), index3 * sizeof(int));
    for (int i = L; i <= R; i++) {
        id[i] = temp[i - L];
    }

    return swaps;
}

int main(){
    int N = getint();
    int D = getint();
    vector<int> W(N);
    vector<pii> id(N), temp(N);
    v.push_back(0);
    for (int i = 0; i < N; ++i) {
        W[i] = getint();
        v.push_back(W[i]);
    }
    sort(v.begin(), v.end());
    auto it = unique(v.begin(), v.end());
    v.resize(distance(v.begin(), it));

    int lptr = 1;
    int rptr = 1;
    n = v.size();
    for (int i = 1; i < n; i++) {
        compress[v[i]] = i;
        while (rptr + 1 < n && v[rptr + 1] < v[i] + D) {
            rptr++;
        }
        r[i] = rptr;
        while (lptr + 1 < n && v[lptr] + D < v[i]) {
            lptr++;
        }
        l[i] = lptr;
        //printf("--%d  %d-> %d %d\n", i, v[i], lptr, rptr);
    }

    for (int i = 0; i < N; ++i) {
        id[i] = {W[i], getint()};
    }
    printf("%lld\n", count_swaps(0, N - 1, id, temp));
	return 0;
}
