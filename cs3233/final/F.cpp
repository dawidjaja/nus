#include <bits/stdc++.h>

using namespace std;

const long long MOD = 1000000007;

long long pngkt(long long a, long long b) {
    if (b == 0) return 1;
    if (b & 1) return (a * pngkt(a, b - 1)) % MOD;
    long long tmp = pngkt(a, b / 2);
    return (tmp * tmp) % MOD;
}

long long fact[200005];

int main() {
    int n;
    scanf("%d", &n);
    fact[0] = 1;
    for (int i = 1; i <= n * 2; i++)
        fact[i] = (fact[i - 1] * i) % MOD;
    long long ans = fact[n * 2];
    ans *= pngkt(fact[n], 2 * (MOD - 2)) % MOD;
    ans %= MOD;
    ans *= pngkt(n + 1, MOD - 2) % MOD;
    ans %= MOD;
    printf("%lld\n", ans);
    return 0;
}
