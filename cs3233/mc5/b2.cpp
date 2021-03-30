#include <bits/stdc++.h>

using namespace std;

#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
typedef long long ll;
typedef pair<int, int> pii;
typedef vector<int> vi;
typedef ll Flow;

struct Edge {
    int dest, back;
    Flow f, c;
};
struct PushRelabel {
    vector<vector<Edge>> g;
    vector<Flow> ec;
    vector<Edge*> cur;
    vector<vi> hs; vi H;
    PushRelabel(int n) : g(n), ec(n), cur(n), hs(2*n), H(n) {}
    void add_edge(int s, int t, Flow cap, Flow rcap=0) {
        if (s == t) return;
        g[s].push_back({t, sz(g[t]), 0, cap});
        g[t].push_back({s, sz(g[s])-1, 0, rcap});
    }
    void add_flow(Edge& e, Flow f) {
        Edge &back = g[e.dest][e.back];
        if (!ec[e.dest] && f) hs[H[e.dest]].push_back(e.dest);
        e.f += f; e.c -= f; ec[e.dest] += f;
        back.f -= f; back.c += f; ec[back.dest] -= f;
    }
    Flow maxflow(int s, int t) {
        int v = sz(g); H[s] = v; ec[t] = 1;
        vi co(2*v); co[0] = v-1;
        rep(i,0,v) cur[i] = g[i].data();
        trav(e, g[s]) add_flow(e, e.c);
        for (int hi = 0;;) {
            while (hs[hi].empty()) if (!hi--) return -ec[s];
            int u = hs[hi].back(); hs[hi].pop_back();
            while (ec[u] > 0) // discharge u
                if (cur[u] == g[u].data() + sz(g[u])) {
                    H[u] = 1e9;
                    trav(e, g[u]) if (e.c && H[u] > H[e.dest]+1)
                        H[u] = H[e.dest]+1, cur[u] = &e;
                    if (++co[H[u]], !--co[hi] && hi < v)
                        rep(i,0,v) if (hi < H[i] && H[i] < v)
                            --co[H[i]], H[i] = v + 1;
                    hi = H[u];
                } else if (cur[u]->c && H[u] == H[cur[u]->dest]+1)
                    add_flow(*cur[u], min(ec[u], cur[u]->c));
                else ++cur[u];
        }
    }
};

int main() {
    int tc;
    scanf("%d", &tc);

    while(tc--) {
        int n, m;
        scanf("%d%d", &n, &m);

        vector<int> score(n + 1);
        for (int i = 0; i < n; i++) {
            scanf("%d", &score[i + 1]);
        }
        int s, t;
        s = 0;
        t = n + m + 1;
        //Dinic d(n + m + 5, s, t);
        PushRelabel d(n + m + 5);


        int rem = m;
        for (int i = 1; i <= m; i++) {
            int a, b;
            scanf("%d%d", &a, &b);

            if (a == n || b == n) {
                score[n] += 2;
                rem--;
            } else {
                d.add_edge(n + i, a, 2);
                d.add_edge(n + i, b, 2);
            }
            d.add_edge(s, n + i, 2);
        }

        int can = 1;
        for (int i = 1; i < n; i++) {
            if (score[i] >= score[n]) {
                can = 0;
                break;
            }
            //printf("-- %d %d -- %d\n", score[n], score[i], score[n] - score[i] - 1);
            d.add_edge(i, t, max(score[n] - score[i] - 1, 0));
        }
        //printf("%d %d %lld\n", can, rem, d.flow());
        if (can && d.maxflow(s, t) == rem * 2) {
            printf("YES\n");
        } else {
            printf("NO\n");
        }

    }
    return 0;
}
