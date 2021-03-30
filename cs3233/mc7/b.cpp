#include <bits/stdc++.h>

#define vi vector<int>
#define vvi vector<vector<int> >

using namespace std;

vector<int> child, par;

int dfs(int cur) {
    if (child[cur] == 0) return 0;
    child[cur] = 0;
    return dfs(par[cur]) + 1;
}

int main() {
    int n;
    scanf("%d", &n);
    child.assign(n, 0);
    par.assign(n, 0);

    for (int i = 0; i < n; i++) {
        int x;
        scanf("%d", &x);
        x--;
        par[i] = x;
        child[x]++;
    }

    queue<int> q;
    for (int i = 0; i < n; i++) {
        if (child[i] == 0) {
            q.push(i);
        }
    }


    vi in(n, 1);
    int ans = 0;
    while (q.size()) {
        int cur = q.front();
        q.pop();
        child[cur] = 0;

        if (in[cur]) {
            ans++;
            in[par[cur]] = 0;
        }

        if (--child[par[cur]] == 0) {
            q.push(par[cur]);
        }

        if (q.empty()) {
            for (int i = 0; i < n; i++) {
                if (child[i] != 0 && in[i] == 0) {
                    q.push(i);
                }
            }
        }
    }
    

    for (int i = 0; i < n; i++) {
        ans += dfs(i) / 2;
    }

    printf("%d\n", ans);
    return 0;
}
