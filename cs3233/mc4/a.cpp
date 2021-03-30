#include <bits/stdc++.h> 
using namespace std;

int moves[6] = {1, 3, 5, 10, 15, 19};
int cost[6] = {1, 1, 2, 1, 2, 3};

int dp[50005];

int main() {
    int tc;
    long long n, ans;
    scanf("%d", &tc);


    for (int i = 0; i <= 50000; i++) {
        dp[i] = 1e9;
    }
    dp[0] = 0;
    for (int i = 0; i < 6; i++) {
        for (int j = moves[i]; j <= 50000; j++) {
            dp[j] = min(dp[j], dp[j - moves[i]] + cost[i]);
        }
    }


    while (tc--) {
        scanf("%lld", &n);

        ans = n / 42750 * 4275;
        ans += dp[n % 42750];
        printf("To obtain %lld points, Adam should execute %lld moves!\n", n, ans);

    }
    return 0;
}

