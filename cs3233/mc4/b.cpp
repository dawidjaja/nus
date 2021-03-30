#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<long long,long long>

using namespace std;

long long tc, c, t, n[15];
double dp[505][505], ans, ks[505][505], p[15];
vector<pii> d[505];

double f(long long cur, long long rem) {
    if (cur == c) {
        return 1;
    }
    if (dp[cur][rem] != -1) {
        return dp[cur][rem];
    }

    double &ret = dp[cur][rem];
    for (long long i = 0; i <= rem; i++) {
        ret = max(ret, ks[cur][i] * f(cur + 1, rem - i));
    }

    return ret;
}

void runKS(long long cur) {
    ks[cur][0] = p[cur] / 100;

    for (int i = 1; i <= t; i++) {
        ks[cur][i] = 0;
    }

    for (long long i = 0; i < n[cur]; i++) {
        for (long long j = t; j - d[cur][i].sc >= 0; j--) {
            ks[cur][j] = max(ks[cur][j], ks[cur][j - d[cur][i].sc] + 1.0 * d[cur][i].fs / 100);
        }
    }

    ks[cur][0] = min(0.9, ks[cur][0]);
    for (long long i = 1; i <= t; i++) {
        ks[cur][i] = min(0.9, max(ks[cur][i - 1], ks[cur][i]));
    }
}

bool cmp(pii a, pii b) {
    return (a.fs * b.sc) > (b.fs * a.sc);
}

int main() {
    scanf("%lld", &tc);
    while (tc--) {
        scanf("%lld%lld", &c, &t);

        for (long long i = 0; i <= c; i++) {
            for (long long j = 0; j <= t; j++) {
                dp[i][j] = -1;
            }
            d[i].clear();
        }

        for (long long i = 0; i < c; i++) {
            long long x;
            scanf("%lld", &x);
            p[i] = x;
        }

        for (long long i = 0; i < c; i++) {
            scanf("%lld", &n[i]);

            for (long long j = 0; j < n[i]; j++) {
                pii tmp;
                scanf("%lld%lld", &tmp.fs, &tmp.sc);
                d[i].push_back(tmp);
            }

            sort(d[i].begin(), d[i].end(), cmp);
            runKS(i);
        }

        ans = f(0, t);
        //printf("%lf\n", ans);
        //ans /= pow(100, c);
        //ans = round(1.0 * ans / 100);
        printf("%.0lf\n", ans * 100);
    }
    return 0;
}

