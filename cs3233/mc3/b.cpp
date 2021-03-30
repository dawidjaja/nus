#include <bits/stdc++.h>

using namespace std;

int tc, m, ln;
map <char, int> letters, lead;
vector<char> chars;
vector <int> val, ans;
int vis[15];
string s;
int ptr, cnt;

struct Numbers {
    int mult[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int total() {
        int ans = 0;
        //printf("total\n");
        for (int i = 0; i < val.size(); i++) {
            //printf("-- %d %d --\n", val[i], mult[i]);
            ans += val[i] * mult[i];
        }
        //printf("ans %d\n", ans);
        return ans;
    }
};

vector <Numbers> v;


bool check() {
    long long tmp = 0;
    for (int i = 1; i < v.size(); i++) {
        tmp += v[i].total();
    }
    if (v[0].total() == tmp) {
        /*
        for (int i = 0; i < v.size(); i++) {
            printf("total %d = %d \n", i, v[i].total());
        }
        */
        //printf("%lld %lld %lld %lld\n", v[0].total(), v[1].total(), v[2].total(), tmp);
        if (cnt == 0) {
            for (int i = 0; i < val.size(); i++) {
                ans.push_back(val[i]);
            }
            cnt++;
        }
        return true;
    }
    return false;
}

int dfs(int cur) {
    //printf("%d\n", cur);
    if (cur == ptr) {
        if (check()) return 1;
        return 0;
    }
    int ret = 0;
    for (int i = 0; i < 10; i++) {
        if (vis[i] == 0) {
            if ((i == 0) && lead[chars[cur]]) {
                continue;
            }
            vis[i] = 1;
            val[cur] = i;

            ret += dfs(cur + 1);

            if (ret >= 2) return ret;
            vis[i] = 0;
        }
    }
    return ret;
}


int main() {
    scanf("%d", &tc);
    getchar();
    while (tc--) {
        cin >> s;

        m = 1;
        letters.clear();
        lead.clear();

        chars.clear();

        val.clear();
        ans.clear();

        cnt = 0;
        ptr = 0;

        v.clear();
        for (int i = 0; i < 10; i++) {
            vis[i] = 0;
        }

        ln = s.length();

        for (int i = 0; i < ln; i++) {
            if ('A' <= s[i] && s[i] <= 'Z') {
                if (!letters.count(s[i])) {
                    chars.push_back(s[i]);
                    letters[s[i]] = 1;
                }
            }
        }
        sort (chars.begin(), chars.end());
        for (int i = 0; i < chars.size(); i++) {
            letters[chars[i]] = i;
            val.push_back(i);
        }
        ptr = chars.size();


        Numbers a;
        v.push_back(a);
        for (int i = ln - 1; i >= 0; i--) {
            if (s[i] == '=' || s[i] == '+') {
                Numbers a;
                v.push_back(a);
                m = 1;
            } else {
                if (i == 0 || s[i - 1] == '=' || s[i - 1] == '+') {
                    lead[s[i]] = 1;
                }
                v[v.size() - 1].mult[letters[s[i]]] += m;
                m *= 10;
            }
        }

        /*
           for (int i = 0; i < v.size(); i++) {
           printf("%d ", v[i].total());
           }
           printf("\n");

           for (int i = 0; i < v.size(); i++) {
           for (int j = 0; j < 8; j++) {
           printf("%d ", v[i].mult[j]);
           }
           printf("\n");
           }
           */

        int tmp = dfs(0);
        if (tmp >= 1) {
            for (int i = 0; i < ln; i++) {
                if ('A' <= s[i] && s[i] <= 'Z') {
                    printf("%d", ans[letters[s[i]]]);
                } else {
                    printf("%c", s[i]);
                }
            }
            if (tmp == 1) {
                printf("*");
            }
            printf("\n");
        } else {
            printf("impossible\n");
        }

    }
    return 0;
}

