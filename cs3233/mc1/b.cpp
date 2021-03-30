#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<int,int>

using namespace std;

int n;
const int tc_num = 7;
int tcs[tc_num] = {8, 9, 10, 7, 500, 777, 1000};
int pos[1005], conf[1005], hitCnt[1005];
priority_queue <pii> pq;


bool hit(int a, int b) {
    if (pos[a] == pos[b] || abs(a - b) == abs(pos[a] - pos[b])) {
        return true;
    }

    return false;
}

int totalConflict() {
    int conflict = 0;
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            if (hit(i, j)) {
                conf[i]++;
                conf[j]++;
                conflict = max(conflict, max(conf[i], conf[j]));
            }
        }
    }

    return conflict;
}

int main() {
    for (int _ = 0; _ < tc_num; _++) {
        n = tcs[_];
        if (n % 6 == 2) {
            int ptr = 0;
            for (int i = 1; i < n; i += 2) {
                pos[ptr++] = i;
            }
            pos[ptr++] = 2;
            pos[ptr++] = 0;
            for (int i = 6; i < n; i += 2) {
                pos[ptr++] = i;
            }

            pos[ptr++] = 4;

        } else if (n % 6 == 3) {

            int ptr = 0;
            for (int i = 3; i < n; i += 2) {
                pos[ptr++] = i;
            }
            pos[ptr++] = 1;
            for (int i = 4; i < n; i += 2) {
                pos[ptr++] = i;
            }

            pos[ptr++] = 0;
            pos[ptr++] = 2;

        } else {
            int ptr = 0;
            for (int i = 1; i < n; i += 2) {
                pos[ptr++] = i;
            }

            for (int i = 0; i < n; i += 2) {
                pos[ptr++] = i;
            }
        }

        if (totalConflict()) {
            int cnt = 0;
            for (int i = 0; i < n; i++) {
                pq.push({-conf[i], i});
            }
            while (true) {
                int cur = pq.top().sc;

                if (pq.top().fs != conf[cur]) {
                    pq.pop();
                    continue;
                }
                if (pq.top().fs == 0) {
                    break;
                }
                pq.pop();

                for (int i = 0; i < n; i++) {
                    hitCnt[i] = 0;
                    if (hit(cur, i)) {
                        conf[i]--;
                    }
                }
                conf[cur] = 0;
                for (int i = 0; i < n; i++) {
                    if (i != cur) {
                        int diff = abs(i - cur);
                        hitCnt[pos[i]]++;
                        if (pos[i] - diff >= 0) {
                            hitCnt[pos[i] - diff]++;
                        }
                        if (pos[i] + diff < n) {
                            hitCnt[pos[i] + diff]++;
                        }
                    }
                }

                int lowest = 0;;
                for (int i = 1; i < n; i++) {
                    if (hitCnt[i] < hitCnt[lowest]) {
                        lowest = i;
                    }
                }
                
                pos[cur] = lowest;
                for (int i = 0; i < n; i++) {
                    if (hit(i, cur)) {
                        conf[i]++;
                        conf[cur]++; 
                        pq.push({-conf[i], i});
                    }
                }
                pq.push({-conf[cur], cur});
            }
        }
        for (int i = 0; i < n; i++) {
            printf("%d%c", pos[i], i == n - 1 ? '\n' : ' ');
        }
    }
    return 0;
}

