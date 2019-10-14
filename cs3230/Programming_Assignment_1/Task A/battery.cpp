#include <bits/stdc++.h>

#define fs first
#define sc second 
#define pii pair<long long,long long>

using namespace std;

const long long INF = 66000000000005;
long long day, cap;
long long n, e, t, vis[100005], sp[100005];
vector<pii> v[100005];

inline int getInt(){
    char c;
    while ((c = getchar_unlocked()) <= ' ');
    int v = 0;
    do { v = (v << 3) + (v << 1) + (c ^ '0'); }
    while ((c = getchar_unlocked()) >= '0' && c <= '9');
    return v;
}

long long shortestPath() {
    for (long long i = 2; i <= n; i++) {
        sp[i] = INF;
    }
    sp[1] = 0;
    priority_queue <pii> pq;
    pq.push({0, 1});
    while (pq.size()) {
        long long cur = pq.top().sc;
        long long len = -pq.top().fs;
        pq.pop();
        if (vis[cur] != day) {
            //printf("-- %lld %lld\n", cur, len);
            vis[cur] = day;
            sp[cur] = len;
            for (long long i = 0; i < v[cur].size(); i++) {
                if (vis[v[cur][i].fs] != day &&
                        v[cur][i].sc <= cap && 
                        sp[v[cur][i].fs] > sp[cur] + v[cur][i].sc) {
                    sp[v[cur][i].fs] = sp[cur] + v[cur][i].sc;
                    pq.push({-sp[v[cur][i].fs], v[cur][i].fs});
                }
            }
        }
    }
    //printf("sp --> %lld\n", sp[n]);
    return sp[n];
}

int main(){
    n = getInt();
    e = getInt();
    t = getInt();

    long long lAns = 1;
    long long rAns = 1;

    for (long long i = 0; i < e; i++) {
        long long a, b, c;
        a = getInt();
        b = getInt();
        c = getInt();
        v[a].push_back({b, c});
        v[b].push_back({a, c});
        rAns = max(rAns, c);
    }

    day = 0;
    while (lAns < rAns) {
        day++;
        cap = ((lAns + rAns) >> 1);
        //printf("-- %lld %lld %lld--\n", cap, lAns, rAns);
        if (shortestPath() <= t && vis[n] == day) {
            rAns = cap;
        } else {
            lAns = cap + 1;
        }
    }
    printf("%lld\n", lAns);
    return 0;
}
