#include <bits/stdc++.h>

using namespace std;

#define fs first
#define sc second
#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
typedef long long ll;
typedef pair<int, int> pii;
typedef vector<int> vi;
typedef vector<vector <int> > vvi;
typedef vector<ll> vll;

bool cmp(pair<int, string> a, pair<int, string> b) {
    return a > b;
}

void solve() {
    int n;
    cin >> n;
    map<string, int> freq;
    for (int i = 0; i < n; i++) {
        string s; cin >> s;
        freq[s]++;
    }

    vector<pair<int,string> > vec;
    for (auto& [k, v]: freq) {
        vec.push_back({v, k});
    }
    sort(vec.begin(), vec.end(), cmp);

    int x = vec.size();
    if (x > 5) x = 5;
    for (int i = 0; i < x; i++) {
        cout << vec[i].sc << ' ' << vec[i].fs << endl;
    }
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(cin.failbit);

    int tc;
    cin >> tc;
    int tmp = 0;
    while (tc--) {
        if (tmp != 0) {
            cout << endl;
        }
        if (tmp == 0) {
            tmp = 1;
        }
        solve();
    }
}
