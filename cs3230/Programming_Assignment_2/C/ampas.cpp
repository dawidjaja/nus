#include <bits/stdc++.h>
using namespace std;

#define LL 					long long
#define DB					double
#define ULL 				unsigned long long
#define PII 				pair<int,int>

#define eb					emplace_back
#define mp					make_pair
#define fs 					first
#define sc 					second
#define psb 				push_back
#define ppb 				pop_back
#define endln 				printf("\n")
#define pii                 pair<int,int>

#define ALL(x)			 	(x).begin(),(x).end()
#define SZ(x)				(int) (x).size()

#define SETMIN(x)			memset((x), -1, sizeof (x))
#define SETNUL(x)			memset((x),  0, sizeof (x))
#define gc					getchar_unlocked

#ifndef getchar_unlocked
#define getchar_unlocked 	getchar
#endif

const int inf = 1e9 + 5;
const LL infll = 1e18+5;
const int mod = 1e9 + 7;

inline int getint(){
    char c;
    while ((c = getchar_unlocked()) <= ' ');
    int v = 0;
    do { v = (v << 3) + (v << 1) + (c ^ '0'); }
    while ((c = getchar_unlocked()) >= '0' && c <= '9');
    return v;
}

const int MAXN = (1 << 18);
const int MAXS = (1 << 11);
const int MAXW = (1 << 11);

int n;
vector <int> items;
int max_w, max_s;
int w[MAXN + 5], s[MAXN + 5];

void readInput() {
    n = getint();
    max_w = max_s = 0;
    items.clear();
    for (int i = 0; i < n; ++i) {
        w[i] = getint();
        max_w = max(max_w, w[i]);
        items.psb(i);
    }
    for (int i = 0; i < n; i++) {
        s[i] = getint();
        max_s = max(max_s, s[i]);
    }
}

int occ[MAXW + MAXS + 5];

void fix_item() {
    vector <int> temp;
    int max_ans = max_w + max_s;
    for (int x : items) {
        if (occ[w[x]] < (max_ans + w[x] - 1) / w[x]) {
            occ[w[x]]++;
            temp.psb(x);
        }
    }
    items = temp;
}

const int LOGN = 20;
int dp[(MAXW + MAXS) * LOGN][MAXW + MAXS + 5];
bool opt[(MAXW + MAXS) * LOGN][MAXW + MAXS + 5] = {};

const int INF = MAXW + MAXS + 1;
//i = idx, j = sisa weight

void backtrack() {
    vector <int> ans;
    int j = INF;
    for (int i = 0; i < items.size(); ++i) {
        if (opt[i][j]) {
            ans.psb(items[i]);
            j = min(j - w[items[i]], s[items[i]]);
        }
    }
    for (int i = ans.size() - 1; i >= 0; --i) {
        cout << ans[i] + 1;
        if (i != 0) {
            printf(" ");
        }
    }
    cout << endl;
}

void counting_sort(vector<int>& items) {
    vector<int> freq[INF];
    for (int i = 0; i < items.size(); i++) {
        freq[w[items[i]] + s[items[i]]].push_back(items[i]);
    }
    items.clear();
    for (int i = INF - 1; i >= 0; i--) {
        for (int j = 0; j < freq[i].size(); j++) {
            items.psb(freq[i][j]);
        }
    }
}

void solve() {
    //preCompLimit();
    counting_sort(items);
    fix_item();
    memset(dp, -1, sizeof(dp));
    for (int i = 0; i <= INF; i++)
        dp[items.size()][i] = 0;

    for (int i = items.size() - 1; i >= 0; i--) {
        for (int j = 0; j <= INF; j++) {
            int &ret = dp[i][j];
            opt[i][j] = false;			//not taking option
            ret = dp[i + 1][j];

            if (j >= w[items[i]]) {
                int temp = w[items[i]] + dp[i + 1][min(j - w[items[i]], s[items[i]])];
                if (temp > ret) {
                    ret = temp;
                    opt[i][j] = true;				//taking option
                }
            }
        }
    }
    //int ans = topdown(0, INF);
    //cout << ans << endl;
    backtrack();
}

int main() {
    readInput();
    solve();
}
