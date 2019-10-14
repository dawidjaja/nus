#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<int,int>

using namespace std;

int n, k, c, a[200005], unik, l, r, vis[200005], jump[200005], ttl;
map <int,int> freq;
vector <pii> cov;

int go(int start, int maks, int x) {
    //printf("%d\n", x);
    if (x == -1) return 0;
    if (vis[x] == 1) return 0;
    if (x > start && x < maks) return 0;
    vis[x] = 1;
    return 1 + go(start, max(maks, x), jump[x]);
}

bool intersect(pii a, pii b) {
    if (a.sc < a.fs) {
        a.sc += ttl;
    }
    if (b.sc < b.fs) {
        b.sc += ttl;
    }
    if (a.sc < b.fs || b.sc < a.fs) {
        return false;
    }
    return true;
}

int main() {
    scanf("%d%d%d", &n, &k, &c);
    for (int i = 0; i < n; i++) {
        scanf("%d", &a[i]);
    }
    l = 0;
    r = 0;
    unik = 1;
    freq[a[l]]++;
    while ((r + 1 < k || unik < c) && r + 1 < n) {
        r++;
        if (freq[a[r]] == 0)
            unik++;
        freq[a[r]]++;
    }
    if (unik < c) {
        printf("0\n");
        return 0;
    }
    while (r - l >= k) {
        if (freq[a[l]] != 1) {
            freq[a[l]]--;
            l++;
        } else {
            break;
        }
    }
    cov.push_back({l, r});
    int R = r;
    int loop = 0;
    int dah = 0;
    while (r < R || loop == 0) {
        r++;
        if (r == n) {
            loop = 1;
            r = 0;
        }
        //printf("awal %d %d %d %d\n", l, r, a[r], freq[a[r]]);
        freq[a[r]]++;
        if (freq[a[r]] == 1) {
            unik++;
        }
        while ((loop == 0 && r - l >= k) ||
                (loop == 1 && r + n - l >= k)) {
            if (unik != c) {
                freq[a[l]]--;
                if (freq[a[l]] == 0) unik--;
                l++;
                if (l == n) {
                    dah = 1;
                    l = l % n;
                }
            } else if(unik == c) {
                if (freq[a[l]] > 1) {
                    freq[a[l]]--;
                    l++;
                    if (l == n) {
                        dah = 1;
                        l = l % n;
                    }
                } else {
                    break;
                }
            }
        }
        if (dah) continue;
        printf("-- %d %d %d --\n", l, r, dah);
        cov.push_back({l, r});
    }
    for (int i = 0; i < cov.size(); i++) {
        printf("final %d %d\n", cov[i].fs, cov[i].sc);
    }
    int ptr = 1;
    ttl = cov.size();
    loop = 0;
    for (int i = 0; i < cov.size(); i++) {
        while ((loop == 0 && cov[i].sc >= cov[ptr].fs) ||
                (loop == 1 && cov[i].sc >= cov[ptr].fs + n)){
            //printf("%d\n", ptr);
            ptr++;
            if (ptr == cov.size()) {
                loop = 1;
                ptr = 0;
            }
        }
        if(intersect(cov[ptr], cov[i])) {
            jump[i] = -1;
        } else {
            printf("- jump - %d %d\n", i, ptr);
            jump[i] = ptr;
        }
    }

    int ans = 0;
    for (int i = 0; i < cov.size(); i++) {
        //printf("%d\n", i);
        if (vis[i] == 0) {
            //printf("%d\n", i);
            ans = max(go(i, i, i), ans);
        }
    }
    printf("%d\n", ans);
    return 0;
}
