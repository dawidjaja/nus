#include <bits/stdc++.h>

using namespace std;

#define fs first
#define sc second
#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
typedef long long ll;
typedef unsigned long long ull;
typedef long double ld;
typedef pair<int, int> pii;
typedef vector<int> vi;
typedef vector<vector <int> > vvi;
typedef vector<ll> vll;

const ll MOD = 1000000007;

ull modmul(ull a, ull b, ull M) {
	ll ret = a * b - M * ull(ld(a) * ld(b) / ld(M));
	return ret + M * (ret < 0) - M * (ret >= (ll)M);
}

ll modpow(ll b, ll e, ll mod) {
	ll ans = 1;
	for (; e; b = b * b % mod, e /= 2)
		if (e & 1) ans = ans * b % mod;
	return ans;
}

vll fact(3000005), inv(3000005, -1), p(3000005, -1);

ll permut(ll n, ll r) {
    return (fact[n] * inv[n - r]) % MOD;
}

ll combi(ll n, ll m, ll p) {
	ll c = 1;
	while (n || m) {
		ll a = n % p, b = m % p;
		if (a < b) return 0;
		c = c * fact[a] % p * inv[b] % p * inv[a - b] % p;
		n /= p; m /= p;
	}
	return c % p;
}

void solve() {
    ll n, m, k;
    cin >> n >> m >> k;

    if (k + 1 == n) {
        //permut(n - 2);
        ll ans = (combi(m, n, MOD) * fact[n - 2]) % MOD;
        cout << ans << endl;
    } else {
        ll ans = 0;

        vll tail(m + 5, -1);
        tail[0] = 0; //modpow(0, n - k - 2, MOD);
        tail[1] = 1; //modpow(1, n - k - 2, MOD);
        vll primes;
        for (int d = k; d < m; d++) {
            //cout << "-- " << d << ' ' << tail[d - k] << endl;
            if (p[d - k] == -1) {
                tail[d - k]  = modpow(d - k, n - k - 2, MOD);
            } else {
                tail[d - k] = (tail[p[d - k]] * tail[(d - k) / p[d - k]]) % MOD;
            }
            ll tmp = (fact[d] * inv[d - k]) % MOD;
            tmp *= tail[d - k];
            tmp = tmp % MOD;
            tmp *= (m - d - 1);
            tmp = tmp % MOD;

            ans += tmp;
            ans = ans % MOD;
        }
        cout << ans << endl;
    }
}

int main() {
	//cin.tie(0)->sync_with_stdio(0);
	//cin.exceptions(cin.failbit);
    int tc;

    int LIM = 3000000;
    ll mod = MOD;

    fact[0] = 1;
    rep(i, 1, LIM + 1) {
        fact[i] = (fact[i - 1] * i) % MOD;
    }


    int x = sqrt(LIM) + 1;
    for (int i = 2; i <= x; i++) {
        if (p[i] == -1) {
            for (int j = i; i * j <= LIM; j++) {
                p[i * j] = i;
            }
        }
    }

    inv[LIM] = modpow(fact[LIM], mod - 2, mod);
    for (int i = LIM - 1; i > 1; i--) {
        if (inv[i] == -1) {
            inv[i] = inv[i + 1] * (i + 1) % mod;
        }
    }

    inv[0] = inv[1] = 1;

    cin >> tc;
    while (tc--) {
        solve();
    }

}
