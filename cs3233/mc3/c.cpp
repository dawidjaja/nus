#include <bits/stdc++.h>

using namespace std;

int tc, start[105], n, maxFib, init[105];

vector<int> ans[10];

set<int> fibo {2, 3, 5, 8, 13, 21, 34, 55, 89};

void fix(int l, int step) {
    int r = l + maxFib;
    ans[step].push_back(l + 1);
    ans[step].push_back(r);
    for (int i = l; i < r; i++) {
        ans[step].push_back(start[i]);
    }
    sort(start + l, start + r);
}

bool correct(int step) {
    for (int i = 0; i < n; i++) {
        if (start[i] != i + 1) return false;
    }
    printf("%d\n", step + 1);
    for (int i = step; i >= 0; i--) {
        for (int j = 0; j < ans[i].size(); j++) {
            if (j == ans[i].size() - 1) {
                printf("%d\n", ans[i][j]);
            } else {
                printf("%d ", ans[i][j]);
            }
        }
    }
    return true;
}

void reset() {
    for (int i = 0; i < n; i++) {
        start[i] = init[i];
    }
    for (int i = 0; i < 3; i++) {
        ans[i].clear();
    }
}

void printAns(int f, vector<int> &v) {
    printf("%d %d", f + 1, f + maxFib);

    for (int i = f; i < f + maxFib; i++) {
        printf(" %d", v[i]);
    }

    printf("\n");
}

int main() {
    scanf("%d\n", &tc);
    while (tc--) {
        scanf("%d", &n);
        for (int i = 0; i < n; i++) {
            scanf("%d", &start[i]);
            init[i] = start[i];
        }

        maxFib = *(--fibo.upper_bound(n));

        if (maxFib == n) {
            printf("1\n1 %d", n);
            for (int i = 0; i < n; i++) {
                printf(" %d", start[i]);
            }
            printf("\n");
            continue;
        }

        reset();

        //printf("-- %d\n", maxFib);

        //2
        fix(0, 0);
        fix(n - maxFib, 1);
        if (correct(1)) {
            continue;
        }

        reset();
        fix(n - maxFib, 0);
        fix(0, 1);
        if (correct(1)) {
            continue;
        }


        //3
        reset();
        vector <vector <int> > st = {{0}, {n - maxFib}};
        vector<int> vt;
        for (int i = 0; i < n - maxFib + 1; i++) {
            vt.push_back(i);
        }
        st.push_back(vt);
        vector <int> order = {0, 1, 2};

        vector<int> asu;
        for (int i = 0; i < n; i++) {
            asu.push_back(init[i]);
        }

        do {
            for (int i = 0; i < st[order[0]].size(); i++) {
                vector<int> u(asu.begin(), asu.end());
                int utmp = st[order[0]][i];
                sort(u.begin() + utmp, 
                        u.begin() + utmp + maxFib);

                for (int j = 0; j < st[order[1]].size(); j++) {
                    vector<int> v(u.begin(), u.end());
                    int vtmp = st[order[1]][j];
                    sort(v.begin() + vtmp, 
                            v.begin() + vtmp + maxFib);

                    for (int k = 0; k < st[order[2]].size(); k++) {
                        vector<int> w(v.begin(), v.end());
                        int wtmp = st[order[2]][k];
                        sort(w.begin() + wtmp,
                                w.begin() + wtmp + maxFib);

                        if (is_sorted(w.begin(), w.end())) {
                            printf("3\n");
                            printAns(wtmp, v);
                            printAns(vtmp, u);
                            printAns(utmp, asu);
                            goto otto;
                        }
                    }
                }
            }
        } while (next_permutation(order.begin(), order.end()));


        reset();
        fix(0, 0);
        fix(n - maxFib, 1);
        fix(0, 2);
        fix(2 * n - 3 * maxFib, 3);
        //assert(false);
        assert(correct(3));

otto: ;
    }
    return 0;
}
