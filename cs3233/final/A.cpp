#include <bits/stdc++.h>

using namespace std;

const long long MOD = 1e9 + 7;
int n, e, q, mat[4005][4005], type, a, b, x;

int main() {
    scanf("%d%d%d", &n, &e, &q);
    for (int i = 0; i < e; i++) {
        scanf("%d%d", &a, &b);
        mat[a][b] = 1;
    }
    int comp = 0;
    int trans = 0;
    while (q--) {
        scanf("%d", &type);
        if (type == 1) {
            n++;
            if (comp == 1) {
                for (int i = 0; i < n - 1; i++) {
                    mat[i][n - 1] = mat[n - 1][i] = 1;
                }
            }
        } else if (type == 2) {
           scanf("%d%d", &a, &b);
           if (trans) swap(a, b);
           mat[a][b] = comp ^ 1;
        } else if (type == 3) {
            scanf("%d", &x);
            for (int i = 0; i < n; i++) {
                if (i == x) continue;
                mat[i][x] = mat[x][i] = comp;
            }
        } else if (type == 4) {
            scanf("%d%d", &a, &b);
            if (trans) swap(a, b);
            mat[a][b] = comp; 
        } else if (type == 5) {
            trans ^= 1;
        } else if (type == 6) {
            comp ^= 1;
        }
    }
    printf("%d\n", n);
    int ex = comp ^ 1;
    for (int i = 0; i < n; i++) {
        long long tujuh = 1;
        long long ans = 0;
        int ttl = 0;
        for (int j = 0; j < n; j++) {
            if (i == j) continue;
            int st = i;
            int en = j;
            if (trans) swap(st, en);
            if (mat[st][en] == ex) {
                ans += (tujuh * j) % MOD;
                ans = ans % MOD;
                tujuh *= 7;
                tujuh = tujuh % MOD;
                ttl++;
            }
        }
        printf("%d %lld\n", ttl, ans);
    }
    return 0;
}
