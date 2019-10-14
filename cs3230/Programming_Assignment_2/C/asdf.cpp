#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<int,int>
#define ppi pair<pii,int>

using namespace std;

int s_max, w_max;
int INF;

int dp[1 << 15][1 << 12], take[1 << 15][1 << 12];
vector<ppi> item;

inline int getint(){
    char c;
    while ((c = getchar_unlocked()) <= ' ');
    int v = 0;
    do { v = (v << 3) + (v << 1) + (c ^ '0'); }
    while ((c = getchar_unlocked()) >= '0' && c <= '9');
    return v;
}

void c_sort(vector<ppi>& arr) {
    vector<ppi> freq[INF];
    for (int i = 0; i < arr.size(); i++) {
        freq[arr[i].fs.fs].push_back(arr[i]);
    }
    arr.clear();
    for (int i = 0; i < INF; i++) {
        for (int j = 0; j < freq[i].size(); j++) {
            arr.push_back(freq[i][j]);
        }
    }
    return;
}

void counting_sort(int n, vector<ppi>& item) {
    vector<ppi> freq[INF];
    for (int i = 0; i < n; i++) {
        freq[item[i].fs.fs + item[i].fs.sc].push_back(item[i]);
    }
    item.clear();
    for (int i = 0; i < INF; i++) {
        c_sort(freq[i]);
        for (int j = 0; j < freq[i].size(); j++) {
            item.push_back(freq[i][j]);
        }
    }
    reverse(item.begin(), item.end());
    return;
}

int select_valid(vector<ppi>& item) {
    int occur[INF];
    memset(occur, 0, sizeof(occur));
    vector<ppi> tmp;
    for (int i = 0; i < item.size(); i++) {
        if (occur[item[i].fs.fs] > (INF / item[i].fs.fs)) continue;
        occur[item[i].fs.fs]++;
        tmp.push_back(item[i]);
    }
    item = tmp;
    return item.size();
}

void backtrack(vector<int>& final_stack) {
    int rem = INF;
    for (int i = 0; i < item.size(); i++) {
        if (take[i][rem]) {
            final_stack.push_back(item[i].sc);
            rem = min(rem - item[i].fs.fs, item[i].fs.sc);
        }
    }
}

vector<int> stack_boxes(int n, vector<int>& W, vector<int>& S) {
    vector<int> final_stack;
    s_max = 0;
    w_max = 0;
    for (int i = 0; i < n; i++) {
        item.push_back({{W[i], S[i]}, i + 1});
        w_max = max(w_max, W[i]);
        s_max = max(s_max, S[i]);
    }
    INF = w_max + s_max + 1;
    counting_sort(n, item);
    n = select_valid(item);

    /*
     *     for (int i = 0; i < n; i++) {
     *             printf("-- %d %d - %d --\n", item[i].fs.fs, item[i].fs.sc, item[i].sc);
     *                 }
     *
     *                     for (int i = 0; i < (1 << 15); i++)
     *                             for (int j = 0; j < (1 << 12); j++)
     *                                         dp[i][j] = -1;
     *
     *                                             */
    memset(dp, -1, sizeof(dp));

    for (int i = 0; i <= INF; i++)
        dp[item.size()][i] = 0;

    for (int i = item.size() - 1; i >= 0; i--) {
        for (int j = 0; j <= INF; j++) {
            int &ret = dp[i][j];

            take[i][j] = 0;
            ret = dp[i + 1][j];

            if (item[i].fs.fs <= j) {
                int tmp = item[i].fs.fs + 
                    dp[i + 1][min(j - item[i].fs.fs, item[i].fs.sc)];
                if (tmp > ret) {
                    ret = tmp;
                    take[i][j] = 1;
                }
            }
        }
    }

    backtrack(final_stack);
    reverse(final_stack.begin(), final_stack.end());
    return final_stack;
}
int main(){
    int N = getint();
    vector<int> W(N);
    for (int i = 0; i < N; ++i) {
        W[i] = getint();
    }
    vector<int> S(N);
    for (int i = 0; i < N; ++i) {
        S[i] = getint();
    }
    int index = 0;
    vector<int> answer = stack_boxes(N, W, S);
    for (int i : answer) {
        printf("%d%c", i, ++index < answer.size() ? ' ' : '\n');
    }
    return 0;
}
