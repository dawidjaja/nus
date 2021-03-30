#include <bits/stdc++.h>

using namespace std;

int tc, n, m;

vector<int> ans[10], start(105);

set<int> fibo {2, 3, 5, 8, 13, 21, 34, 55, 89};

void printAns(int f, vector<int> &v) {
    printf("%d %d", f + 1, f + m);

    for (int i = f; i < f + m; i++) {
        printf(" %d", v[i]);
    }

    printf("\n");
}

int main() {
    scanf("%d", &tc);
    while (tc--) {
        scanf("%d", &n);

        vector<int> init(n);
        for (int i = 0; i < n; i++) {
            scanf("%d", &init[i]);
        }

        m = *(--fibo.upper_bound(n));

        if (n == m) {
            printf("1\n");
            printf("1 %d", n);
            for (int i = 0; i < n; i++) {
                printf(" %d", init[i]);
            }
            printf("\n");
            continue;
        }

        vector<int> ut (init.begin(), init.end());
        sort(ut.begin(), ut.begin() + m);
        vector<int> vt (ut.begin(), ut.end());
        sort(vt.begin() + n - m, vt.begin() + n);

        if (is_sorted(vt.begin(), vt.end())) {
            printf("2\n");
            printAns(n - m, ut);
            printAns(0, init);
            continue;
        }

        vector<int> ut1 (init.begin(), init.end());
        sort(ut1.begin() + n - m, ut1.begin() + n);
        vector<int> vt1 (ut1.begin(), ut1.end());
        sort(vt1.begin(), vt1.begin() + m);

        if (is_sorted(vt1.begin(), vt1.end())) {
            printf("2\n");
            printAns(0, ut1);
            printAns(n - m, init);
            continue;
        }

        vector<vector<int> > s = {{0}, {n - m}};
        vector<int> tt;
        for (int i = 0; i < n - m + 1; i++) {
            tt.push_back(i);
        }

        s.push_back(tt);
        vector<int> o = {0, 1, 2};

        do {
            for (int i = 0; i < s[o[0]].size(); i++) {
                vector<int> u(init.begin(), init.end());
                int utmp = s[o[0]][i];
                sort(u.begin() + utmp,
                        u.begin() + utmp + m);

                for (int j = 0; j < s[o[1]].size(); j++) {
                    vector<int> v(u.begin(), u.end());
                    int vtmp = s[o[1]][j];
                    sort(v.begin() + vtmp,
                            v.begin() + vtmp + m);

                    for (int k = 0; k < s[o[2]].size(); k++) {
                        vector<int> w(v.begin(), v.end());
                        int wtmp = s[o[2]][k];
                        sort(w.begin() + wtmp,
                                w.begin() + wtmp + m);

                        if (is_sorted(w.begin(), w.end())) {
                            printf("3\n");

                            printAns(wtmp, v);
                            printAns(vtmp, u);
                            printAns(utmp, init);
                            goto otto;
                        }
                    }

                }
            }
        } while (next_permutation(o.begin(), o.end()));
        
        {
            vector<int> a (init.begin(), init.end());
            sort(a.begin(), a.begin() + m);
            vector<int> b (a.begin(), a.end());
            sort(b.begin() + n - m, b.begin() + n);
            vector<int> c (b.begin(), b.end());
            sort(c.begin(), c.begin() + m);
            

            printf("4\n");
            printAns(2 * n - 3 * m, c);
            printAns(0, b);
            printAns(n - m, a);
            printAns(0, init);
        }

otto: ;
    }

    return 0;
}

