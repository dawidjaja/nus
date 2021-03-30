#include <bits/stdc++.h>

using namespace std;

int main() {
    long long tc;
    scanf("%lld", &tc);
    while (tc--) {
        long long n, b;
        long long const MOD = 1e9 + 7;
        scanf("%lld%lld", &n, &b);

        vector<long long> p(n + 1), good(n + 1, 1);
        p[0] = 0;
        for (long long i = 1; i <= n; i++) {
            scanf("%lld", &p[i]);
        }

        for (long long i = 0; i < b; i++) {
            long long x;
            scanf("%lld", &x);

            good[x] = 0;
        }

        stack<long long> suf;
        vector<long long> dp(n + 1), ps(n + 1);
        long long count = 0;
        dp[0] = 1;
        ps[0] = 1;
        suf.push(0);

        for (long long i = 1; i <= n; i++) {
            while (suf.size() >= 2 && p[suf.top()] > p[i]) {
                long long b = suf.top();
                suf.pop();
                long long a = suf.top();
                //printf("-- pop %lld %lld --\n", a, b);

                long long fr = a == 0 ? 0 : ps[a - 1];
                count -= good[b] * (ps[b - 1] - fr);
                count = count % MOD;
            }

            //printf("-- plus %lld - %lld %lld %lld\n", count, good[i], i, suf.top());
            long long fr = suf.top() == 0 ? 0 : ps[suf.top() - 1];
            count += good[i] * (ps[i - 1] - fr);
            count = count % MOD;
            //.printf("-- new count %lld ", count);
            suf.push(i);

            dp[i] = dp[i - 1] + count;
            dp[i] = dp[i] % MOD;
            ps[i] = ps[i - 1] + dp[i];
            ps[i] = ps[i] % MOD;
            //printf("-- %lld %lld %lld--\n", i, dp[i], count);
        }


        dp[n] = dp[n] % MOD;
        dp[n] = dp[n] + MOD;
        long long ans = (dp[n] - 1) % MOD;
        printf("%lld\n", ans);

    }
    return 0;
}
