#include <bits/stdc++.h>

using namespace std;

long long tc, n, q, last;
long long S, M, h[100005], cur;
const long long INF = LLONG_MAX;
deque <long long> dq;
set <long long> pre, suf;

void init() {
    scanf("%lld%lld", &n, &q);

    pre.clear();
    suf.clear();
    dq.clear();

    h[0] = h[n + 1] = 0;

    S = 0;
    M = 0;
    for (long long i = 1; i <= n; i++) {
        scanf("%lld", &h[i]);
        S += h[i];
        M = max(M, h[i]);
    }

    for (long long i = 1; i <= n; i++) {
        if (dq.empty() || h[i] > h[dq.back()]) {
            dq.push_back(i);
        }
    }

    cur = 0;

    last = 0;
    while (dq.size()) {
        cur += (dq.front() - last) * h[last];
        last = dq.front();
        pre.insert(dq.front());
        dq.pop_front();
    }
    cur += ((n + 1) - last) * h[last];
    pre.insert(n + 1);

    for (long long i = n; i > 0; i--) {
        if (dq.empty() || h[i] > h[dq.back()]) {
            dq.push_back(i);
        }
    }
    dq.push_back(0);

    last = n;
    while (dq.size()) {
        cur += (last - dq.front()) * h[last];
        last = dq.front();
        suf.insert(-dq.front());
        dq.pop_front();
    }
    cur += last * h[last];
    suf.insert(0);
    printf("%lld\n", cur - n * M - S);

    /*
    for (auto it = pre.begin(); it != pre.end(); it++) {
        printf("pre %lld\n", *it);
    }
    for (auto it = suf.begin(); it != suf.end(); it++) {
        printf("suf %lld\n", *it);
    }
    */
    h[0] = h[n + 1] = INF;
}

int main() {
    scanf("%lld", &tc);

    while (tc--) {

        init();

        while (q--) {
            long long x, p;
            scanf("%lld%lld", &x, &p);
            /*
            printf("PRE ->>>>>>>>>>>>>>");
            for (auto it = pre.begin(); it != pre.end(); it++) {
                printf(" %lld", *it);
            }
            printf("\nSUF ->>>>>>>>>>>>>>");
            for (auto it = suf.begin(); it != suf.end(); it++) {
                printf(" %lld", *it);
            }
            printf("\n");
            printf(" START CUR %lld\n", cur);
            */
            long long tmp;
            long long last = x;

            long long hl = pre.upper_bound(x) == pre.begin() ? 0 : h[*(--pre.upper_bound(x))];

            if (hl < h[x] + p) {
                long long r = *(pre.upper_bound(x));
                cur += (r - x) * (h[x] + p - hl);
                //printf("otto cur %lld\n", cur);

                while (tmp = *pre.upper_bound(x), h[tmp] < h[x] + p) {
                    //printf("-tes pre- %lld\n", tmp);
                    long long k = *(pre.upper_bound(tmp));
                    cur += (k - tmp) * (h[x] + p - h[tmp]);
                    pre.erase(tmp);
                }
                pre.insert(x);
            }
            
            //printf(" MID CUR %lld\n", cur);

            hl = suf.upper_bound(-x) == suf.begin() ? 0 : h[-(*(--suf.upper_bound(-x)))];
            //printf("suf hl %lld\n", hl);
            last = x;
            if (hl < h[x] + p) {
                long long r = -(*(suf.upper_bound(-x)));
                cur += (x - r) * (h[x] + p - hl);
                //printf("ini tgh suf dan cur %lld %lld\n", r, cur);

                while (tmp = -(*(suf.upper_bound(-x))), h[tmp] < h[x] + p) {
                    //printf("-tes suf- %lld\n", -tmp);
                    long long k = -(*(suf.upper_bound(-tmp)));
                    cur += (tmp - k) * (h[x] + p - h[tmp]);
                    suf.erase(-tmp);
                }

                suf.insert(-x);
            }
            
            //printf(" FINAL CUR %lld\n", cur);

            h[x] += p;
            M = max(M, h[x]);
            S += p;

            printf("%lld\n", cur - n * M - S);

        }

    }
    return 0;
}
