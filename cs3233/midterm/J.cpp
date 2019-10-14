#include <bits/stdc++.h>

using namespace std;

int n, q, price[10], gem[200005], bit[7][200005], type, k, p;

inline void update(int gemType, int idx, int x) {
    for (int i = idx; i <= n; i += (i & -i)) {
        bit[gemType][i] += x;
    }
}

long long get(int gemType, int l, int r) {
    long long ret = 0;
    for (int i = r; i > 0; i -= (i & -i)) {
        ret += bit[gemType][i];
    }
    for (int i = l - 1; i > 0; i -= (i & -i)) {
        ret -= bit[gemType][i];
    }
    return ret;
}

int main() {
    scanf("%d%d", &n, &q);
    for (int i = 1; i <= 6; i++) {
        scanf("%d", &price[i]);
    }
    for (int i = 1; i <= n; i++) {
        scanf("%1d", &gem[i]);
        update(gem[i], i, 1);
    }
    while (q--) {
        scanf("%d%d%d", &type, &k, &p);
        if (type == 1) {
            for (int i = 1; i <= 6; i++) {
                if (get(i, k, k) == 1) {
                    update(i, k, -1);
                }
            }
            update(p, k, 1);
        } else if (type == 2) {
            price[k] = p;
        } else if (type == 3) {
            long long tmp = 0;
            for (int i = 1; i <= 6; i++) {
                tmp += get(i, k, p) * price[i];
            }
            printf("%lld\n", tmp);
        }
    }
    return 0;
}
