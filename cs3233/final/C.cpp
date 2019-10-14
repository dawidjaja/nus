#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<int,int>

using namespace std;

long long ans;
char l[200005], r[200005];
const long long MOD = 1e9 + 7;
long long fact[400005];
const int MAX = 200001;
int valid[10] = {1, 1, 1, 1, -1, 1, 0, 1, 0, 1};
long long pjgl, pjgr;

long long pngkt(long long a, long long b) {
    if (b == 0) return 1;
    if (b & 1) return (a * pngkt(a, b - 1)) % MOD;
    long long tmp = pngkt(a, b / 2);
    return (tmp * tmp) % MOD;
}

long long combi(long long a, long long b) {
    long long ret = fact[a];
    ret *= pngkt(fact[b], MOD - 2);
    ret = ret % MOD;

    ret *= pngkt(fact[a - b], MOD - 2); 
    ret = ret % MOD;
    return ret;
}

long long gnjl(int x) {
    long long ret = 0;
    for (int i = 1; i <= (x - 1) / 2; i++) {
        long long tmp;
        tmp = pngkt(2, i);
        tmp = tmp % MOD;

        tmp *= pngkt(7, i - 1);
        tmp = tmp % MOD;

        tmp *= 13;
        tmp = tmp % MOD;

        tmp *= combi(2 * i - 1, i - 1);
        tmp = tmp % MOD;

        ret += tmp;
        ret = ret % MOD;
    }
    //printf("ret -- %lld\n", ret);
    return ret;
}

long long f_valid(long long k, long long a, long long b) {
    if (k - b - 1 < 0 || k - a < 0) return 0;

    long long tmp = combi(2 * k - a - b - 1, k - a);

    tmp *= pngkt(2, k - a);
    tmp = tmp % MOD;

    tmp *= pngkt(7, k - b - 1);
    tmp = tmp % MOD;

    return tmp;
}

long long f_inval(long long k, long long a, long long b) {
    if (k - b < 0 || k - a - 1 < 0) return 0;

    long long tmp = combi(2 * k - a - b - 1, k - b);

    tmp *= pngkt(2, k - a - 1);
    tmp = tmp % MOD;

    tmp *= pngkt(7, k - b);
    tmp = tmp % MOD;

    return tmp;
}

void update(pii& ctr, int x) {
    if (valid[x] == 0) {
        ctr.fs++;
    } else if(valid[x] == 1) {
        ctr.sc++;
    }
}

int count_valid(int st, int cur) {
    int ctr = 0;
    for (int i = (st == 0 ? 1 : 0); i < cur; i++) {
        if (valid[i] == 1) ctr++;
    }
    return ctr;
}

int count_inval(int cur) {
    int ctr = 0;
    for (int i = 0; i < cur; i++) {
        if (valid[i] == 0) ctr++;
    }
    return ctr;
}

long long countr() {
    pii ctr = {0, 0};
    long long ans = gnjl(pjgr);

    if (pjgr & 1) return ans;

    for (int i = 0; i < pjgr; i++) {
        long long cval = count_valid(i, r[i] - '0');
        long long cinv = count_inval(r[i] - '0');

        long long hsl_valid = f_valid(pjgr / 2, ctr.fs, ctr.sc);
        long long hsl_inval = f_inval(pjgr / 2, ctr.fs, ctr.sc);

        ans += (cval * hsl_valid) % MOD;
        ans = ans % MOD;

        ans += (cinv * hsl_inval) % MOD;
        ans = ans % MOD;

        if (r[i] - '0' == 4) {
            break;
        }
        update(ctr, r[i] - '0');
    }
    return ans;
}

long long countl() {
    pii ctr = {0, 0};
    long long ans = gnjl(pjgl);

    if (pjgl & 1) return ans;

    for (int i = 0; i < pjgl; i++) {
        long long cval = count_valid(i, l[i] - '0');
        long long cinv = count_inval(l[i] - '0');

        long long hsl_valid = f_valid(pjgl / 2, ctr.fs, ctr.sc);
        long long hsl_inval = f_inval(pjgl / 2, ctr.fs, ctr.sc);

        ans += (cval * hsl_valid) % MOD;
        ans = ans % MOD;

        ans += (cinv * hsl_inval) % MOD;
        ans = ans % MOD;
        if (l[i] - '0' == 4) {
            break;
        }
        update(ctr, l[i] - '0');
    }
    return ans;
}

void mines() {
    for (int i = pjgl - 1; i >= 0; i--) {
        if (l[i] != '0') {
            l[i]--;
            break;
        }
        l[i] = '9';
    }
    if (l[0] == '0' && pjgl != 1) {
        //cout << l << endl;
        int tmp = pjgl;
        for (int i = 0; i < tmp - 1; i++) {
            l[i] = l[i + 1];
        }
        l[tmp - 1] = '\0';
        //pjgl--;
    }
}

void proceed_corner() {
    pii ctr = {0, 0};
    for (int i = 0; i < pjgr; i++) {
        if (r[i] == '4') {
            ctr = {1, 0};
            break;
        }
        if (valid[r[i] - '0'] == 1) {
            ctr.fs++;
        } else if (valid[r[i] - '0'] == 0) {
            ctr.sc++;
        }
    }
    if (ctr.fs == ctr.sc) {
        ans++;
    }
    ctr = {0, 0};
    for (int i = 0; i < pjgl; i++) {
        if (l[i] == '4') {
            ctr = {1, 0};
            break;
        }
        if (valid[l[i] - '0'] == 1) {
            ctr.fs++;
        } else if (valid[l[i] - '0'] == 0) {
            ctr.sc++;
        }
    }
    if (ctr.fs == ctr.sc) {
        ans--;
    }
}

int main() {
    scanf("%s", l);
    scanf("%s", r);
    pjgl = strlen(l);
    pjgr = strlen(r);
    fact[0] = 1;
    for (int i = 1; i <= 400004; i++) {
        fact[i] = (fact[i - 1] * i) % MOD;
    }
    long long ka = countr();
    assert(l[0] != '0');
    assert(r[0] != '0');
    mines();

    //cout << l << endl;

    long long ki = countl();
    ans = ka - ki;
    proceed_corner();
    //printf("%lld %lld\n", ki, ka);
    printf("%lld\n", ans);
    return 0;
}
