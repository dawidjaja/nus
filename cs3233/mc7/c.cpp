#include <bits/stdc++.h>

using namespace std;

typedef vector<bool> vb;

typedef vector<int> vi;
typedef vector<vi> vvi;

int calc_sum(int N, int P, bitset<324> &used_nodes, vvi &total_A) {
    bitset<324> byte_mask((1 << P) - 1);
    int acc = 0;
    for (int type = 0; type < N; type++) {
        auto nodes_in_type = (used_nodes >> (type * P) & byte_mask).to_ullong();
        acc += total_A[type][nodes_in_type];
    }
    return acc;
}

int main() {
    iostream::sync_with_stdio(false), cin.tie(nullptr);
    int N, P, S;
    cin >> N >> P >> S;
    vi A(N * P);
    vvi AL(N * P);

    // region Input section
    for (int i = 0; i < N * P; i++) cin >> A[i];
    for (int i = 0; i < S; i++) {
        int x, y;
        cin >> x >> y;
        AL[x - 1].push_back(y - 1);
        AL[y - 1].push_back(x - 1);
    }
    // endregion

    vector<bitset<324>> walk_mask(N * P);
    // region Populating walk_mask
    for (int start_node = 0; start_node < N * P; start_node++) {
        stack<int> node_stack;
        vb visited(N * P);
        node_stack.push(start_node);
        while (!node_stack.empty()) {
            auto top_node = node_stack.top();
            node_stack.pop();
            if (visited[top_node]) continue;
            visited[top_node] = true;
            walk_mask[start_node].set(top_node);
            for (auto neighbour : AL[top_node]) {
                if (visited[neighbour] || (neighbour / P) == (start_node / P)) continue;
                node_stack.push(neighbour);
            }
        }
    }
    // endregion

    vvi total_A(N, vi(1 << P));
    // region Populating total_A memo
    for (int type = 0; type < N; type++) {
        for (int i = 1; i < (1 << P); i++) {
            int idx = __builtin_ctz(i);
            int prev_mask = i - (i&-i);
            total_A[type][i] = total_A[type][prev_mask] + A[type * P + idx];
        }
    }
    // endregion

    int best_total = 0;
    for (int bad_type = 0; bad_type < N; bad_type++) {
        vector<pair<bitset<324>, int>> graph_memo(1 << P, pair(bitset<324>(), INT_MIN));
        graph_memo[0] = pair(bitset<324>(), 0);
        for (int bad_mask = 1; bad_mask < (1 << P); bad_mask++) {
            for (int tmp = bad_mask; tmp > 0; tmp -= (tmp & -tmp)) {
                int new_idx = __builtin_ctz(tmp);
                int new_lsb = 1 << new_idx;
                int prev_mask = bad_mask - new_lsb;
                if (graph_memo[prev_mask].second == INT_MIN) continue;
                if (prev_mask != 0) {
                    if ((graph_memo[prev_mask].first & walk_mask[bad_type * P + new_idx]).none()) continue;
                }
                graph_memo[bad_mask].first = graph_memo[prev_mask].first | walk_mask[bad_type * P + new_idx];
                graph_memo[bad_mask].second = calc_sum(N, P, graph_memo[bad_mask].first, total_A);
                bitset<324> bitset_bad_mask(bad_mask);
                bitset_bad_mask <<= (bad_type * P);
                auto total_bad = total_A[bad_type][bad_mask];
                /*cout << bitset_bad_mask << ": ";
                cout << total_bad << endl;
                cout << graph_memo[bad_mask].first << ": ";
                cout << graph_memo[bad_mask].second << endl;*/
                graph_memo[bad_mask].second -= N * total_bad;
                best_total = max(best_total, graph_memo[bad_mask].second);
                break;
            }
        }
        
        vb visited(N*P);
        for (int start_node = 0; start_node < N*P; start_node++) {
            if (start_node / P == bad_type) continue;
            if (visited[start_node]) continue;

            int total_score = 0;
            stack<int> node_stack;
            node_stack.push(start_node);
            while (!node_stack.empty()) {
                auto top_node = node_stack.top();
                node_stack.pop();
                if (visited[top_node]) continue;
                visited[top_node] = true;
                total_score += A[top_node];
                for (int neighbour : AL[top_node]) {
                    if (visited[neighbour] || neighbour / P == bad_type) continue;
                    node_stack.push(neighbour);
                }
            }

            best_total = max(total_score, best_total);
        }
    }
    cout << best_total << '\n';
}