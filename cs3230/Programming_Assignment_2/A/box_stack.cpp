#include <bits/stdc++.h>

#define fs first
#define sc second
#define pii pair<int,int>

using namespace std;

int n;
pii item[200005];

inline int getInt(){
	char c;
	while ((c = getchar_unlocked()) <= ' ');
	int v = 0;
	do { v = (v << 3) + (v << 1) + (c ^ '0'); }
	while ((c = getchar_unlocked()) >= '0' && c <= '9');
	return v;
}

int main() {
    n = getInt();
    for (int i = 0; i < n; i++) {
        item[i].fs = getInt();
        item[i].sc = i + 1;
    }
    for (int i = 0; i < n; i++) {
        int tmp = getInt();
        item[i].fs += tmp;
    }

    sort(item, item + n);

    for (int i = 0; i < n; i++) {
        printf("%d", item[i].sc);
        if (i == n - 1) {
            printf("\n");
        } else {
            printf(" ");
        }
    }
    
	return 0;
}
