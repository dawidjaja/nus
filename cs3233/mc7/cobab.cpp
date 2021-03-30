#include<cstdio>
#include<vector>
#include<stack>
using namespace std;

vector<int> in, p;

int dfs(int v) {
    if (in[v] == 0) return 0;
    in[v] = 0;
    return 1 + dfs(p[v]);
}

int main() {
    int n; scanf("%d", &n);

    p.assign(n, 0); in.assign(n, 0);
    for (int i = 0; i < n; i++) {
        int x; scanf("%d", &x);
        p[i] = x - 1;
    }

    for (int i = 0; i < n; i++)
        in[p[i]]++;

    vector<bool> con(n, true);
    stack<int> leaves;
    for (int i = 0; i < n; i++)
        if (in[i] == 0)
            leaves.push(i);

    int mis = 0;
    while (!leaves.empty()) {
        int v = leaves.top(); leaves.pop();
        in[v] = 0;
        if (con[v]) {
            mis++;
            con[p[v]] = false;
        }
        if(--in[p[v]] == 0)
            leaves.push(p[v]);

        if (leaves.empty()) {
            for (int i = 0; i < n; i++)
                if (in[i] != 0 && con[i] == false)
                    leaves.push(i);
        }
    }

    for (int i = 0; i < n; i++)
        mis += dfs(i) / 2;
    printf("%d\n", mis);
