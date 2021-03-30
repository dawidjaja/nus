#include <bits/stdc++.h>
using namespace std;
typedef long long ll;
typedef vector<int> vi;
typedef vector<ll> vll;
typedef pair<int,int> pii;
typedef vector<pii> vii;
typedef tuple<int,int,int> iii;
typedef vector<iii> viii;
#define endl '\n'
#define umap unordered_map
#define uset unordered_set
#define pb push_back
#define LSOne(x) (x & (-x))
#define all(x) x.begin(), x.end()
#define rall(x) x.rbegin(), x.rend()
#define sz(x) (int) x.size()
#define rep(i, a, b) for (int i = a; i < b; ++i)
#define trav(a, x) for (auto& a : x)
const int IINF = 1e9 + 7, MAX = 3e6;
const ll INF = 1e18 + 7, mod = IINF;

vi status, smol;
vll fac, inv, memo;

ll modPow(ll a, ll b, ll mod) {
    ll ret = 1;
    for (; b; a = a * a % mod, b /= 2) if (b & 1) ret = ret * a % mod;
    return ret;
}

void sieve() {
	for (int i = 4; i <= MAX; i += 2)
		smol[i] = 2, status[i] = true;
	for (int i = 3; i * i <= MAX; i++) if (!status[i])
        for (int j = i * i; j <= MAX; j += i + i)
            smol[j] = i, status[j] = true;
}

ll chooseModP(ll n, ll m, ll p) {
	ll c = 1;
	while (n || m) {
		ll a = n % p, b = m % p;
		if (a < b) return 0;
		c = c * fac[a] % p * inv[b] % p * inv[a - b] % p;
		n /= p; m /= p;
	}
	return c % p;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    status.resize(MAX + 1, 0);
    smol.resize(MAX + 1, -1);
    sieve();
    fac.resize(MAX + 1);
    inv.resize(MAX + 1, -1);
    inv[0] = 1, inv[1] = 1;
    fac[0] = 1, fac[1] = 1;
    rep(i, 2, MAX + 1) fac[i] = (i * fac[i-1]) % mod;
    inv[MAX] = modPow(fac[MAX], mod - 2, mod);
    for (int i = MAX - 1; i > 1; --i) if (inv[i] == -1)
        inv[i] = inv[i + 1] * (i + 1) % mod;
    int tc;
    cin >> tc;
    while (tc--) {
        ll n, m, k;
        cin >> n >> m >> k;
        ll ret = 0;
        memo.assign(m + 1, -1);
        memo[0] = 0, memo[1] = 1;
        if (k + 1 == n) {
            ret = chooseModP(m, n, mod) * fac[n - 2] % mod;
        } else rep(d, k, m) {
            ll x = d - k;
            ll p = smol[x];
            if (p == -1) memo[x] = modPow(x, n - k - 2, mod);
            else memo[x] = memo[p] * memo[x/p] % mod;
            ll cur = (m - d - 1) * fac[d] % mod * inv[x] % mod * memo[x] % mod;
            ret = (ret + cur) % mod;
        }
        cout << ret << endl;
    }
}
