#include <bits/stdc++.h>

using namespace std;

#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
typedef long long ll;
typedef pair<int, int> pii;
typedef vector<int> vi;

bool dfs(int a, int L, vector<vi>& g, vi& btoa, vi& A, vi& B) {
    if (A[a] != L) return 0;
    A[a] = -1;
    trav(b, g[a]) if (B[b] == L + 1) {
        B[b] = 0;
        if (btoa[b] == -1 || dfs(btoa[b], L + 1, g, btoa, A, B))
            return btoa[b] = a, 1;
    }
    return 0;
}
int hopcroftKarp(vector<vi>& g, vi& btoa) {
    int res = 0;
    vi A(g.size()), B(btoa.size()), cur, next;
    for (;;) {
        fill(all(A), 0);
        fill(all(B), 0);
        cur.clear();
        trav(a, btoa) if(a != -1) A[a] = -1;
        rep(a,0,sz(g)) if(A[a] == 0) cur.push_back(a);
        for (int lay = 1;; lay++) {
            bool islast = 0;
            next.clear();
            trav(a, cur) trav(b, g[a]) {
                if (btoa[b] == -1) {
                    B[b] = lay;
                    islast = 1;
                }
                else if (btoa[b] != a && !B[b]) {
                    B[b] = lay;
                    next.push_back(btoa[b]);
                }
            }
            if (islast) break;
            if (next.empty()) return res;
            trav(a, next) A[a] = lay;
            cur.swap(next);
        }
        rep(a,0,sz(g))
            res += dfs(a, 0, g, btoa, A, B);
    }
}

int main() {

    cin.tie(NULL);
    int tc;
    scanf("%d", &tc);
    while (tc--) {
        int n;
        scanf("%d", &n);
        vi btoa(n, -1);
        vector<vi> g(n);
        for (int i = 0; i < n; i++) {
            char c;
            cin >> c;
            //getchar();
            for (int j = 0; j < n; j++) {
                //char c = getchar();
                cin >> c;
                if (c == 'W') {
                    g[i].push_back(j);
                }
            }
        }

        int ans = hopcroftKarp(g, btoa);

        if (ans == n) {
            printf("YES\n");
        } else {
            printf("NO\n");
        }
    }

    return 0;
}
