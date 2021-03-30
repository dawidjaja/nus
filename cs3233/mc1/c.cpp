#include <bits/stdc++.h>

using namespace std;

unordered_set <long long> s;
unordered_set <long long> fins;

long long k, m, p, inv, tc;

long long pwr(long long a, long long b) {
    if (b == 0) {
        return 1;
    }
    if (b == 1) {
        return a % p;
    }
    long long tmp = pwr(a, b / 2);
    tmp *= tmp;
    tmp = tmp % p;
    if (b & 1) {
        tmp *= a;
        tmp = tmp % p;
    }
    return tmp;
}

int main() {
    scanf("%lld", &tc);
    for (long long _ = 0; _ < tc; _++) {
        scanf("%lld%lld%lld", &k, &m, &p);

        //printf("ini tc %lld %lld %lld\n", k, m, p);
        if (m == 0) {
            // cannot
            printf("OK, bummer :(\n");
        } else if (k == p - 1) {
            if (m != p - 1) {
                //cannot
                printf("OK, bummer :(\n");
            } else {
                //take 1 - p-1;
                printf("O(K), boomer\n");
                for (long long i = 1; i <= k; i++) {
                    printf("%lld%c", i, i == k ? '\n' : ' ');
                }
            }
        } else {
            inv = 0;
            if (k > p / 2) {
                inv = 1;
                m = ((p - 1) * pwr(m, p - 2)) % p;
                k = p - 1 - k;
            }
            
            while (true) {
                long long tmp = 1;
                s.clear();
                for (long long i = 0; i < k - 1; i++) {
                    long long x = (rand() % (p - 2)) + 1;
                    while (s.count(x)) {
                        x = rand() % (p - 2) + 1;
                    }
                    s.insert(x);
                    tmp *= x;
                    tmp = tmp % p;
                }

                long long g = (m * pwr(tmp, p - 2)) % p;
                
                if (!s.count(g)) {
                    s.insert(g);
                    break;
                }
            }

            printf("O(K), boomer\n");
            fins.clear();
            if (inv) {
                for (long long i = 1; i < p; i++) {
                    if (!s.count(i)) {
                        fins.insert(i);
                    }
                }
            } else {
                fins = s;
            }
            for (auto it = fins.begin(); it != fins.end(); it++) {
                if (it != fins.begin()) {
                    printf(" ");
                }
                printf("%lld", *it);
            }
            printf("\n");
        }
    }
    return 0;
}
