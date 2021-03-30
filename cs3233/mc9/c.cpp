#include <bits/stdc++.h>

using namespace std;

#define fs first
#define sc second
//#define endl '\n'
#define rep(i, a, b) for(int i = a; i < (b); ++i)
#define trav(a, x) for(auto& a : x)
#define all(x) begin(x), end(x)
#define sz(x) (int)(x).size()
typedef long long ll;
typedef pair<int, int> pii;
typedef vector<int> vi;
typedef vector<vector <int> > vvi;
typedef vector<ll> vll;

struct SuffixArray {
	vi sa, lcp;
	SuffixArray(string& s, int lim=256) { // or basic_string<int>
		int n = sz(s) + 1, k = 0, a, b;
		vi x(all(s)+1), y(n), ws(max(n, lim)), rank(n);
		sa = lcp = y, iota(all(sa), 0);
		for (int j = 0, p = 0; p < n; j = max(1, j * 2), lim = p) {
			p = j, iota(all(y), n - j);
			rep(i,0,n) if (sa[i] >= j) y[p++] = sa[i] - j;
			fill(all(ws), 0);
			rep(i,0,n) ws[x[i]]++;
			rep(i,1,lim) ws[i] += ws[i - 1];
			for (int i = n; i--;) sa[--ws[x[y[i]]]] = y[i];
			swap(x, y), p = 1, x[sa[0]] = 0;
			rep(i,1,n) a = sa[i - 1], b = sa[i], x[b] =
				(y[a] == y[b] && y[a + j] == y[b + j]) ? p - 1 : p++;
		}
		rep(i,1,n) rank[sa[i]] = i;
		for (int i = 0, j; i < n - 1; lcp[rank[i++]] = k)
			for (k && k--, j = sa[rank[i] - 1];
					s[i + k] == s[j + k]; k++);
	}
};

vi lg(100005);

int getMin(vvi &c, int l, int r) {
    int j = lg[r - l + 1];
    return min(c[j][l], c[j][r - (1 << j) + 1]);
}

void solve() {
    int n;
    string s;
    cin >> n;
    cin >> s;

    int ans = 0;

    vvi c(20);
    rep(i, 0, 20) {
        c[i] = vi(n);
    }

    SuffixArray sa(s);
    unordered_map <int, vi> pos;
    //pos[0].push_back(-1);
    for (int i = 0; i < n; i++) {
        c[0][i] = (i == 0 ? 0 : c[0][i - 1]) +  (s[i] == '(' ? 1 : -1);
        if (pos[c[0][i]].size() == 0) pos[c[0][i]].push_back(-1);
        pos[c[0][i]].push_back(i);
    }

    for (int i = 1; i < 20; i++) {
        int jump = 1 << (i - 1);
        for(int j = 0; j + (1 << i) <= n; j++) {
            c[i][j] = min(c[i - 1][j], c[i - 1][j + jump]);
            //cout << j << ' ' << i << ' ' << c[i][j] << endl;
        }
    }

    for (int i = 1; i < n + 1; i++) {
        int slen = n - sa.sa[i];
        int cur = sa.sa[i];
        int start = sa.lcp[i] + cur;
        //cout << "pos " << cur << endl;

        int l = cur - 1;
        int r = n - 1;

        while (l < r) {
            int mid = ((l + r) >> 1) + 1;
            if (getMin(c, cur, mid) < (cur != 0 ? c[0][cur - 1] : 0)) {
                r = mid - 1;
            } else {
                l = mid;
            }
        }
        //cout << "atas: " << start << ' ' << l << endl;
        if (l < start) {
            continue;
        }

        int dl, ul, dr, ur;
        dl = 0;
        ul = 0;
        int g = cur != 0 ? c[0][cur - 1] : 0;
        dr = ur = pos[g].size() - 1;

        while (dl < dr) {
            int mid = ((dl + dr) >> 1) + 1;
            //cout << "pos: " << g << ' ' << mid << ' ' << pos[g][mid] << endl;
            if (pos[g][mid] >= start) {
                dr = mid - 1;
            } else {
                dl = mid;
            }
        }
        //cout << "dldr: " << dl << ' ' << dr << endl;

        while (ul < ur) {
            int mid = floor((1.0 * (ul + ur) / 2)) + 1;
            //cout << ul << ' ' << ur << ' ' << mid << endl;
            if (pos[g][mid] > l) {
                ur = mid - 1;
            } else {
                ul = mid;
            }
        }
        //cout << "d u: " << dl << ' ' << ul << endl;
        ans += ul - dl;
        //cout << "ans: " << ans << endl;


    }

    cout << ans << endl;
}

int main() {
	cin.tie(0)->sync_with_stdio(0);
	cin.exceptions(cin.failbit);

    int tc;
    cin >> tc;

    lg[1] = 0;
    for (int i = 2; i < 100000; i++) {
        lg[i] = lg[i / 2] + 1;
    }

    while (tc--) {
        solve();
    }

}
