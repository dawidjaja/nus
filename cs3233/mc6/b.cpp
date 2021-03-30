#include <bits/stdc++.h> 
using namespace std;

#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
#define fs first
#define sc second
typedef long long ll;
typedef pair<int, int> pii;
typedef vector<int> vi;

vi val, comp, z, cont;
int Time, ncomps;

template<class G> int dfs(int j, G& g) {
    int low = val[j] = ++Time, x; z.push_back(j);
    trav(e,g[j]) if (comp[e] < 0)
        low = min(low, val[e] ?: dfs(e,g));
    if (low == val[j]) {
        do {
            x = z.back(); z.pop_back();
            comp[x] = ncomps;
            cont.push_back(x);
        } while (x != j);
        cont.clear();
        ncomps++;
    }
    return val[j] = low;
}
template<class G> void scc(G& g) {
    int n = sz(g);
    val.assign(n, 0); comp.assign(n, -1);
    Time = ncomps = 0;
    rep(i,0,n) if (comp[i] < 0) dfs(i, g);
}

bool dfs(int a, int L, vector<vi>& g, vi& btoa, vi& A, vi& B) { if (A[a] != L) return 0;
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
    int tc;
    scanf("%d", &tc);
    while (tc--) {
        int n;
        scanf("%d", &n);
        vector<vi> g(n);
        vector<vi> v(2 * n);
        for (int i = 0; i < n; i++) {
            while (true) {
                int x;
                scanf("%d", &x);
                if (x == 0) break;
                g[i].push_back(x - 1);
            }
        }
        vi btoa(n, -1);
        hopcroftKarp(g, btoa);
        for (int i = 0; i < n; i++) {
            for (int nx : g[i]) {
                if (btoa[nx] == i) {
                    //printf("a- %d %d --\n", n + nx, i);
                    v[n + nx].push_back(i);
                } else {
                    //printf("b- %d %d --\n", i, n + nx);
                    v[i].push_back(n + nx);
                }
            }
        }

        scc(v);
        //printf("ncomps %d\n", ncomps);
        vi freq(2 * n);

        for(int i = 0; i < 2 * n; i++) {
            //printf("comp %d %d\n", i, comp[i]);
            freq[comp[i]]++;
        }

        vector<pii> ans;
        for (int i = n; i < 2 * n; i++) {
            //printf("freq -- %d %d--\n", i, freq[i]);
            if (freq[comp[i]] == 1) {
                ans.push_back({btoa[i - n] + 1, i - n + 1});
            }
        }
        sort(ans.begin(), ans.end());
        if (ans.size() == 0) {
            printf("-1\n");
        } else {
            for (pii cur: ans) {
                printf("%d %d\n", cur.fs, cur.sc);
            }
        }
        printf("\n");
    }
    return 0;
}
