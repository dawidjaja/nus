#include <bits/stdc++.h>

using namespace std;

#define fs first
#define sc second
#define rep(i, a, b) for(ll i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
typedef long long ll;
typedef pair<int, int> pii;
typedef vector<int> vi;
typedef vector<vector <int> > vvi;
typedef vector<ll> vll;

ll a, b, m;

ll modpow(ll b, ll e, ll mod = m) {
	ll ans = 1;
	for (; e; b = b * b % mod, e /= 2)
		if (e & 1) ans = ans * b % mod;
	return ans;
}

ll chooseModP(ll n, ll m, int p, vll& fact, vll& invfact) {
	ll c = 1;
	while (n || m) {
		ll a = n % p, b = m % p;
		if (a < b) return 0;
		c = c * fact[a] % p * invfact[b] % p * invfact[a - b] % p;
		n /= p; m /= p;
	}
	return c;
}

void solve() {
    cin >> a >> b >> m;

    vll fact(m), invfact(m);
    fact[0] = fact[1] = 1;
    invfact[0] = invfact[1] = 1;
    rep(i, 2, m) {
        fact[i] = (fact[i - 1] * i) % m;
        invfact[i] = modpow(fact[i], m - 2) % m;
    }

    cout << chooseModP(a + b, a, m, fact, invfact) << endl;

}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(cin.failbit);
    ll tc;
    cin >> tc;

    while (tc--) {
        solve();
    }
    return 0;
}
