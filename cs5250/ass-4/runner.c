#include <stdio.h>
#include <stdlib.h>
#include <assert.h>
#include <string.h>

int pages_seq[512];
int pages_num[512];
int order[512];
int par[512];

int ina_seqno[250];
int ina_page[250];
int act_seqno[250];
int act_page[250];
int ina_ptr = 0;
int act_ptr = 0;

typedef struct {
    int fs, sc;
} Pair;

Pair* pair(int a, int b) {
    Pair* p = malloc(sizeof(Pair));
    p->fs = a;
    p->sc = b;
    return p;
}

int getBuddy(int cur, int ord) {
    if (cur & (cur - 1) == 0) {
        return cur + ord;
    } else {
        return cur - ord;
    }
}

int min(int a, int b) {
    return a < b ? a : b;
}

void mergeBuddy(int cur) {
    while (order[cur] < 512) {
        int b = getBuddy(par[cur], order[cur]);
        if (order[b] == order[cur]) {
            for (int i = 0; i < order[b]; i++) {
                if (pages_seq[b + i] != -1 ||
                    pages_seq[par[cur] + i] != -1) {
                    return;
                }
            }
            int st = min(par[cur], b);
            for (int i = 0; i < order[b]; i++) {
                order[st + i] *= 2;
            }
        } else {
            return;
        }

    }
}


int insertBuddy(int seqno, int num, int len) {
    for (int i = 0; i < 512; i++) {
        if (pages_seq[i] != -1) {
            assert(order[i] > 0);
            i += order[i];
            i--;
        } else {
            assert(order[i] > 0);
            if (order[i] < len) {
                i += order[i];
                i--;
                continue;
            }

            while (order[i] / 2 >= len) {
                assert(order[i] > 1);
                
                int new_order = order[i] / 2;
                int old_order = order[i];
                for (int j = 0; j < old_order; j++) {
                    order[i + j] = new_order;
                    assert(order[i + j] != 0);
                    par[i + j] = (j < new_order) ? i : (i + new_order);
                    assert(par[i + j] <= i + j);
                }

            }
            
            for (int j = 0; j < len; j++) {
                pages_seq[i + j] = seqno;
                pages_num[i + j] = num + j;
            }
            
            return 1;
        }
    }
    return 0;
}

void breakBuddy(int x) {
    while (order[x] != 1) {
        int p = par[x];

        int old_order = order[p];
        int new_order = order[p] / 2;
        for (int i = 0; i < old_order; i++) {
            order[p + i] = new_order;
            assert(order[p + i] != 0);
            par[p + i] = (i < new_order) ? p : (p + new_order);
            assert(par[p + i] <= p + i);
        }
    }
}

void removeBuddy(int seqno, int num) {
    int idx = -1;
    for (int i = 0; i < 512; i++) {
        if (pages_seq[i] == seqno && pages_num[i] == num) {
            idx = i;
            break;
        }
    }

    if (idx != -1) {
        breakBuddy(idx);

        pages_seq[idx] = -1;
        pages_num[idx] = -1;

        mergeBuddy(idx);
    }
}

Pair* insertList(int *sq, int *pg, int *iptr, int seqno, int num) {
    Pair* p = pair(-1, -1);
    if (*iptr == 250) {
        p = pair(sq[0], pg[0]);
        removeBuddy(sq[0], pg[0]);
        for (int i = 1; i < 250; i++) {
            sq[i - 1] = sq[i];
            pg[i - 1] = pg[i];
        }
        *iptr -= 1;
    }

    sq[*iptr] = seqno;
    pg[*iptr] = num;
    (*iptr)++;
    return p;
}

void insertInactive(int seqno, int num, int len) {
    for (int i = 0; i < len; i++) {
        insertList(ina_seqno, ina_page, &ina_ptr, seqno, i);
    }
    assert(len != 0);
    int succ = insertBuddy(seqno, num, len);
    if (!succ) {
        for (int i = 0; i < len; i++) {
            insertBuddy(seqno, num + i, 1);
        }
    }
}

void insertActive(int seqno, int num, int len) {
    for (int i = 0; i < len; i++) {
        Pair *p = insertList(act_seqno, act_page, &act_ptr, seqno, i);
        if (p->fs != -1 && p->sc != -1) {
            insertInactive(p->fs, p->sc, 1);
        }
    }
}

void removeFromList(int *sq, int *pg, int *iptr, int seqno, int num) {

    int idx = -1;
    for (int i = 0; i < *iptr; i++) {
        if (sq[i] == seqno && pg[i] == num) {
            idx = i;
            break;
        }
    }
    for (int i = idx + 1; i < *iptr; i++) {
        sq[i - 1] = sq[i];
        pg[i - 1] = pg[i];
    }
}

void removeInactive(int seqno, int num) {
    removeFromList(ina_seqno, ina_page, &ina_ptr, seqno, num);
}

void removeActive(int seqno, int num) {
    removeFromList(act_seqno, act_page, &act_ptr, seqno, num);
}

int isInList(int *sq, int *pg, int *iptr, int seqno, int num) {
    for (int i = 0; i < *iptr; i++) {
        if (sq[i] == seqno && pg[i] == num) {
            return 1;
        }
    }

    return 0;
}

int isInactive(int seqno, int num) {
    return isInList(ina_seqno, ina_page, &ina_ptr, seqno, num);

}

int isActive(int seqno, int num) {
    return isInList(act_seqno, act_page, &act_ptr, seqno, num);
}

int main() {
    char c;
    int seqno, num;
    memset(pages_seq, -1, sizeof(pages_seq));
    memset(pages_num, -1, sizeof(pages_num));
    for (int i = 0; i < 512; i++) {
        order[i] = 512;
        par[i] = 0;
    }

    while (scanf(" %c %d %d", &c, &seqno, &num) != EOF) {

        switch(c) {
            case 'A':
                // allocate pages
                insertInactive(seqno, 0, num);
                break;
            case 'X':
                // access
                if (isInactive(seqno, num)) {
                    removeInactive(seqno, num);
                    insertActive(seqno, num, 1);
                } else if (isActive(seqno, num)) {
                } else {
                    insertInactive(seqno, num, 1);
                }
                break;

            case 'F':
                if (isInactive(seqno, num)) {
                    removeInactive(seqno, num);
                } else if (isActive(seqno, num)) {
                    removeActive(seqno, num);
                }
                break;
        }
    }

    printf("Buddy Allocator Final States:\n");
    for (int i = 0; i < 512; i++) {
        printf("Buddy[%.3d] with group id %d and length %d, (seq, page) = (%d, %d)\n",
                i, par[i], order[i], pages_seq[i], pages_num[i]);
    }

    printf("Inactive list final states:\n");
    for (int i = 0; i < ina_ptr; i++) {
        printf("LRU Inactive[%.3d], (seqno, page) = (%d, %d)\n",
                i, ina_seqno[i], ina_page[i]);
    }

    printf("Active list final states:\n");
    for (int i = 0; i < act_ptr; i++) {
        printf("LRU Active[%.3d], (seqno, page) = (%d, %d)\n",
                i, act_seqno[i], act_page[i]);
    }

    

    return 0;
}
