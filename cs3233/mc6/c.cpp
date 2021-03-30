#include <bits/stdc++.h>

using namespace std;

#define fs first
#define sc second

#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
typedef long long ll;
typedef pair<int, int> pii;
typedef pair<pii, int> ppi;
typedef vector<int> vi;
typedef vector<double> vd;

bool zero(double x) { return fabs(x) < 1e-10; }

int MinCostMatching(const vector<vi>& cost, vi& L, vi& R) {
    int n = sz(cost), mated = 0;
    vi dist(n), u(n), v(n);
    vi dad(n), seen(n);
    rep(i,0,n) {
        u[i] = cost[i][0];
        rep(j,1,n) u[i] = min(u[i], cost[i][j]);
    }
    rep(j,0,n) {
        v[j] = cost[0][j] - u[0];
        rep(i,1,n) v[j] = min(v[j], cost[i][j] - u[i]);
    }
    L = R = vi(n, -1);
    rep(i,0,n) rep(j,0,n) {
        if (R[j] != -1) continue;
        if (zero(cost[i][j] - u[i] - v[j])) {
            L[i] = j;
            R[j] = i;
            mated++;
            break;
        }
    }
    for (; mated < n; mated++) { // un t i l so lution is f eas i b l e
        int s = 0;
        while (L[s] != -1) s++;
        fill(all(dad), -1);
        fill(all(seen), 0);
        rep(k,0,n)
            dist[k] = cost[s][k] - u[s] - v[k];
        int j = 0;
        for (;;) {
            j = -1;
            rep(k,0,n){
                if (seen[k]) continue;
                if (j == -1 || dist[k] < dist[j]) j = k;
            }
            seen[j] = 1;
            int i = R[j];
            if (i == -1) break;
            rep(k,0,n) {
                if (seen[k]) continue;
                auto new_dist = dist[j] + cost[i][k] - u[i] - v[k];
                if (dist[k] > new_dist) {
                    dist[k] = new_dist;
                    dad[k] = j;
                }
            }
        }
        rep(k,0,n) {
            if (k == j || !seen[k]) continue;
            auto w = dist[k] - dist[j];
            v[k] += w, u[R[k]] -= w;
        }
        u[s] += dist[j];
        while (dad[j] >= 0) {
            int d = dad[j];
            R[j] = R[d];
            L[R[j]] = j;
            j = d;
        }
        R[j] = s;
        L[s] = j;
    }
    auto value = 0;
    rep(i,0,n) value += cost[i][L[i]];
    return value;
}

int p[105];
int par(int x) {
    if (p[x] == x) return x;
    return p[x] = par(p[x]);
}

void dfs(int to, int cur, int w, int e, vector<ppi> &tree, vector<vi> &g, vi &lvl) {
    //printf("-- %d %d %d %d --\n", to, cur, w, e);

    if (cur == to) return;
    while (lvl[cur] < lvl[to]) {
        //printf("while %d\n", cur);
        g[tree[cur].sc][e] = -max(0, tree[cur].fs.sc - w);
        cur = tree[cur].fs.fs;
    }

    while (cur != to) {
        /*
        printf("while %d %d\n", cur, to);
        printf("te %d %d\n", tree[cur].sc, e);
        printf("kntl %d\n", g.size());
        printf("asu %d %d\n", tree[cur].sc, g[tree[cur].sc].size());
        */

        g[tree[cur].sc][e] = -max(0, tree[cur].fs.sc - w);
        cur = tree[cur].fs.fs;

        g[tree[to].sc][e] = -max(0, tree[to].fs.sc - w);
        to = tree[to].fs.fs;
    }
}

void travtree(int cur, int pr, vector<ppi> &tree, vi &lvl, vector<vector< ppi> > &v) {
    for (ppi nx: v[cur]) {
        if (nx.fs.fs != pr) {
            tree[nx.fs.fs] = {{cur, nx.fs.sc}, nx.sc};
            lvl[nx.fs.fs] = lvl[cur] + 1;
            travtree(nx.fs.fs, cur, tree, lvl, v);
        }
    }
}

int main() {
    int tc;
    scanf("%d", &tc);
    while (tc--) {
        int n, m;
        scanf("%d%d", &n, &m);
        
        vector <vector<ppi> > v(n);
        vector <ppi> edge(m);
        for (int i = 0; i < n; i++) {
            p[i] = i;
        }
        for (int i = 0; i < m; i++) {
            int a, b, c;
            scanf("%d%d%d", &a, &b, &c);
            a--;
            b--;

            edge[i] = {{a, b}, c};
            if (i < n - 1) {
                v[a].push_back({{b, c}, i});
                v[b].push_back({{a, c}, i});
                p[par(a)] = par(b);
            }
        }

        int nc = 0;
        for (int i = 1; i < n; i++) {
            if (par(i) != par(0)) {
                nc = 1;
                break;
            }
        }
        if (nc) {
            printf("-1\n");
            continue;
        }

        vector<ppi> tree(n);
        vi lvl(n);
        //printf("otto\n");
        travtree(0, -1, tree, lvl, v);

        //printf("otto\n");

        vector<vi> g(n - 1);
        for (vi &cur: g) {
            for (int i = 0; i < m - n + 1; i++) {
                cur.push_back(0);
            }
        }
        if (m - n + 1 == 0) {
            printf("0\n");
            continue;
        }
        //printf("gedenya %d %d\n", n - 1, m - n + 1);

        for (int i = n - 1; i < m; i++) {
            if (lvl[edge[i].fs.fs] > lvl[edge[i].fs.sc]) {
                swap(edge[i].fs.fs, edge[i].fs.sc);
            }
            //printf("- %d \n", i);
            dfs(edge[i].fs.fs,
                edge[i].fs.sc,
                edge[i].sc,
                i - n + 1,
                tree,
                g, 
                lvl);
            //printf("- %d \n", i);
        }

        vi L, R;
        printf("%d\n", -MinCostMatching(g, L, R));
        
    }
}
