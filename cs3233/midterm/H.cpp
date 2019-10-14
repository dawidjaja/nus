#include <bits/stdc++.h>

using namespace std;

bool cmp(int a, int b) {
    return b > a;
}

int main() {
    scanf("%d", &n);
    for (int i = 0; i < n; i++) {
        scanf("%d", &rocks[i]);
    }
    sort(rocks, rocks + n, cmp);
    for (int i = 0; i < n - 1; i++) {
        if (can(rocks[i], rocks[i - 1])) {

        }
    }
    return 0;
}
