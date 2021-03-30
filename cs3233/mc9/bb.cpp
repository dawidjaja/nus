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
typedef vector<string> vs;

vi pi(const string& s) {
    vi p(sz(s));
    rep(i,1,sz(s)) {
        int g = p[i-1];
        while (g && s[i] != s[g]) g = p[g-1];
        p[i] = g + (s[i] == s[g]);
    }
    return p;
}
vi match(const string& s, const string& pat) {
    vi p = pi(pat + '\0' + s), res;
    rep(i,sz(p)-sz(s),sz(p)) {
        if (p[i] == sz(pat)) {
            res.push_back(i - 2 * sz(pat));
            break;
        }
    }
    return res;
}


struct SuffixArray {
    string txt;
    vi sa, lcp;
    SuffixArray(string& s, int lim=256) { // or basic_string<int>
        txt = s;
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

    int search(string &pat, int l = 0, int r = -1) {
        if (r == -1) r = sa.size();
        int n = txt.size(), m = pat.size();
        if (r - l == 1) {
            bool valid = strncmp(pat.c_str(), txt.c_str() + sa[l], m) == 0;
            return valid ? l : -1;
        }

        int p = (l + r) / 2;
        int cmp = strncmp(pat.c_str(), txt.c_str() + sa[p], m);
        if (cmp < 0) return search(pat, l, p);
        return search(pat, p, r);
    }

};

int main() {
    cin.tie(0)->sync_with_stdio(0);
    cin.exceptions(cin.failbit);

    int l;
    cin >> l;

    vs songs(l);
    vector<SuffixArray> vecs;

    for (int i = 0; i < l; i++) {
        cin >> songs[i];
        int n = songs[i].length();

        SuffixArray sa(songs[i]);
        int ans = 0;
        for (int i = 0; i < n + 1; i++) {
            if (sa.lcp[i] > sa.lcp[ans]) {
                ans = i;
            }
        }

        songs[i] = songs[i].substr(sa.sa[ans], sa.lcp[ans]);
        vecs.push_back(SuffixArray(songs[i]));
        //cout << songs[i] << endl;
    }

    int q;
    cin >> q;

    while (q--) {
        string t;
        cin >> t;
        bool first = true;
        vi ans;
        for (int i = 0; i < l; i++) {

            int panda = vecs[i].search(t);
            if (panda != -1) ans.push_back(i);
        }
        if (ans.size() == 0) {
            cout << "-1\n";
        } else {
            int first = 1;
            for (int x: ans) {
                if (first) {
                    cout << x;
                } else {
                    cout << ' ' << x;
                }
                first = 0;
            }
            cout << '\n';
        }
    }

}
