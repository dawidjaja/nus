#include <bits/stdc++.h>
#include <bits/extc++.h>

#define fs first
#define sc second

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

const ll INF = numeric_limits<ll>::max() / 4;
typedef vector<ll> VL;
struct MCMF {
    int N;
    vector<vi> ed, red;
    vector<VL> cap, flow, cost;
    vi seen;
    VL dist, pi;
    vector<pii> par;
    MCMF(int N) :
        N(N), ed(N), red(N), cap(N, VL(N)), flow(cap), cost(cap),
        seen(N), dist(N), pi(N), par(N) {}
    void add_edge(int from, int to, ll cap, ll cost) {
        this->cap[from][to] += cap;
        if (this->cap[from][to] == cap) {
            this->cost[from][to] = cost;
            ed[from].push_back(to);
            red[to].push_back(from);
        }
    }
    void path(int s) {
        fill(all(seen), 0);
        fill(all(dist), INF);
        dist[s] = 0; ll di;
        __gnu_pbds::priority_queue<pair<ll, int> > q;
        vector<decltype(q)::point_iterator> its(N);
        q.push({0, s});
        auto relax = [&](int i, ll cap, ll cost, int dir) {
            ll val = di - pi[i] + cost;
            if (cap && val < dist[i]) {
                dist[i] = val;
                par[i] = {s, dir};
                if (its[i] == q.end()) its[i] = q.push({-dist[i], i});
                else q.modify(its[i], {-dist[i], i});
            }
        };
        while (!q.empty()) {
            s = q.top().second; q.pop();
            seen[s] = 1; di = dist[s] + pi[s];
            trav(i, ed[s]) if (!seen[i])
                relax(i, cap[s][i] - flow[s][i], cost[s][i], 1);
            trav(i, red[s]) if (!seen[i])
                relax(i, flow[i][s], -cost[i][s], 0);
        }
        rep(i,0,N) pi[i] = min(pi[i] + dist[i], INF);
    }
    pair<ll, ll> maxflow(int s, int t) {
        ll totflow = 0, totcost = 0;
        while (path(s), seen[t]) {
            ll fl = INF;
            for (int p,r,x = t; tie(p,r) = par[x], x != s; x = p)
                fl = min(fl, r ? cap[p][x] - flow[p][x] : flow[x][p]);
            totflow += fl;
            for (int p,r,x = t; tie(p,r) = par[x], x != s; x = p)
                if (r) flow[p][x] += fl;
                else flow[x][p] -= fl;
        }
        rep(i,0,N) rep(j,0,N) totcost += cost[i][j] * flow[i][j];
        return {totflow, totcost};
    }
    // I f some costs can be negative , c a l l th is before maxflow :
    void setpi(int s) { // (otherwise , leave th is out)
        fill(all(pi), INF); pi[s] = 0;
        int it = N, ch = 1; ll v;
        while (ch-- && it--)
            rep(i,0,N) if (pi[i] != INF)
                trav(to, ed[i]) if (cap[i][to])
                if ((v = pi[i] + cost[i][to]) < pi[to])
                    pi[to] = v, ch = 1;
        assert(it >= 0); // negative cost cyc le
    }
};

int md(int x, int n) {
    return (x + n) % n;
}

int main() {
    int tc;
    scanf("%d", &tc);
    while(tc--) {
        int n, m;
        int ttlf = 0;
        scanf("%d%d", &n, &m);

        vector<int> l(m), r(m), a(m), b(m), h(n), f(m), x(m);
        int s = n;
        int t = n + 1;
        PushRelabel pr(n + 2);

        for (int i = 0; i < m; i++) {
            scanf("%d%d%d%d", &l[i], &r[i], &a[i], &b[i]);
            l[i]--;
            r[i]--;
            x[i] = b[i] - a[i];
            h[l[i]] += a[i];
            h[md(r[i] + 1, n)] -= a[i];
            int tmp = md(r[i] - l[i] + 1, n);
            if (tmp == 0) tmp = n;
            ttlf += a[i] * tmp;
            pr.add_edge(md(r[i] + 1, n), l[i], x[i]);
        }

        int ttlh = 0;
        int tcur = 0;
        for (int i = 0; i < n; i++) {
            ttlh += max(0, h[i]);
        }

        for (int i = 0; i < n; i++) {
            if (h[i] > 0) {
                pr.add_edge(s, i, h[i]);
            } else if (h[i] < 0) {
                pr.add_edge(i, t, -h[i]);
            }
        }


        if (pr.maxflow(s, t) == ttlh) {
            MCMF mcmf(n + 2);
            for (int i = 0; i < n; i++) {
                if (h[i] > 0) {
                    mcmf.add_edge(s, i, h[i], 0);
                } else if (h[i] < 0) {
                    mcmf.add_edge(i, t, -h[i], 0);
                }
            }

            for (int i = 0; i < m; i++) {
                int cst = md(r[i] - l[i] + 1, n);
                if (cst == 0) cst += n;
                mcmf.add_edge(md(r[i] + 1, n), l[i], x[i], cst);
            }

            int tmp = mcmf.maxflow(s, t).sc;

            printf("%d\n", (tmp + ttlf) / n);
        } else {
            printf("-1\n");
        }

    }
    return 0;
}
