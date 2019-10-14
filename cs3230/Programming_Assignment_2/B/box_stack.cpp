#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<int,int>

using namespace std;

inline int getint(){
	char c;
	while ((c = getchar_unlocked()) <= ' ');
	int v = 0;
	do { v = (v << 3) + (v << 1) + (c ^ '0'); }
	while ((c = getchar_unlocked()) >= '0' && c <= '9');
	return v;
}
vector<int> stack_boxes(int n, int s, vector<int>& w) {
    vector<int> final_stack;
    pii max_val = {0, 0};
    for (int i = 0; i < n; i++) {
        pii tmp = {w[i], i + 1};
        max_val = max(tmp, max_val);
    }

    vector<int> bt(s + 1);
    bt[0] = 0;
    for (int i = 1; i <= s; i++) {
        bt[i] = -1;
    }
    for (int i = 0; i < n; i++) {
        if (i + 1 == max_val.sc) continue;
        for (int j = s; j - w[i] >= 0; j--) {
            if (bt[j - w[i]] == -1 || bt[j] != -1) continue;
            bt[j] = i + 1;
        }
    }
    for (int i = s; i >= 0; i--) {
        if (bt[i] != -1) {
            int cur = i;
            while (cur != 0) {
                final_stack.push_back(bt[cur]);
                cur = cur - w[bt[cur] - 1];
            }
            break;
        }
    }
    final_stack.push_back(max_val.sc);
    return final_stack;
}
int main(){
    int N = getint();
    int S = getint();
    vector<int> W(N);
    for (int i = 0; i < N; ++i) {
        W[i] = getint();
    }
    int index = 0;
    vector<int> answer = stack_boxes(N, S, W);
    for (int i : answer) {
        printf("%d%c", i, ++index < answer.size() ? ' ' : '\n');
    }
	return 0;
}
