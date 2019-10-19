import sys
import copy
from collections import deque

class Sudoku(object):
    def __init__(self, puzzle):
        self.nines = [0, 1, 2, 3, 4, 5, 6, 7, 8]
        # you may add more attributes if you need
        self.puzzle = puzzle # self.puzzle is a list of lists
        self.ans = copy.deepcopy(puzzle) # self.ans is a list of lists

        self.domain = []
        for line in puzzle:
            tmp = []
            for elem in line:
                if (elem == 0):
                    tmp.append(set([1, 2, 3, 4, 5, 6, 7, 8, 9]))
                else:
                    tmp.append(set([elem]))
            self.domain.append(tmp)

        self.adj = []
        for i in self.nines:
            tmp = []
            for j in self.nines:
                tmp.append(set())
            self.adj.append(tmp)

        for i in self.nines:
            for j in self.nines:
                for k in xrange(j + 1, 9):
                    self.adj[i][j].add((i, k))
                    self.adj[i][k].add((i, j))
        for i in self.nines:
            for j in self.nines:
                for k in xrange(j + 1, 9):
                    self.adj[j][i].add((k, i))
                    self.adj[k][i].add((j, i))
        for i in xrange(3):
            for j in xrange(3):
                tmp = []
                for k in self.nines:
                    tmp.append((i * 3 + k // 3, j * 3 + (k % 3)))
                for k in xrange(len(tmp)):
                    for l in xrange(k + 1, len(tmp)):
                        self.adj[tmp[k][0]][tmp[k][1]].add(tmp[l])
                        self.adj[tmp[l][0]][tmp[l][1]].add(tmp[k])

    def revise(self, x, y):
        if (len(self.domain[y[0]][y[1]]) == 1):
            elem = next(iter(self.domain[y[0]][y[1]]))
            if (elem in self.domain[x[0]][x[1]]):
                self.domain[x[0]][x[1]].discard(elem)
                return True
        return False

    def ac(self):
        # q = Queue.Queue()
        q = deque()
        for i in self.nines:
            for j in self.nines:
                for k in self.adj[i][j]:
                    q.append(((i, j), k))
        
        while (len(q) != 0):
            (x, y) = q.popleft()
            # print(x, y)
            if (self.revise(x, y)):
                if (self.domain[x[0]][x[1]] == set()):
                    return False
                for neigh in self.adj[x[0]][x[1]]:
                    if (neigh != y):
                        q.append((neigh, x))
        return True

    def dfs(self, y, x):
        if (y == 9 and x == 0):
            return True
        # copy by value
        curDomain = copy.deepcopy(self.domain)

        for i in curDomain[y][x]:
            self.domain = copy.deepcopy(curDomain)
            self.domain[y][x] = set([i])
            if (self.ac()):
                if (self.dfs(y + (x + 1) // 9, (x + 1) % 9)):
                    return True
        return False

    def solve(self):
        #TODO: Your code here

        # don't print anything here. just resturn the answer
        # self.ans is a list of lists

        self.dfs(0, 0)
        for i in self.nines:
            for j in self.nines:
                self.ans[i][j] = next(iter(self.domain[i][j]))
        # self.prettyprint(self.domain)

        return self.ans

    # you may add more classes/functions if you think is useful
    # However, ensure all the classes/functions are in this file ONLY

if __name__ == "__main__":
    # STRICTLY do NOT modify the code in the main function here
    if len(sys.argv) != 3:
        print ("\nUsage: python sudoku_A2_xx.py input.txt output.txt\n")
        raise ValueError("Wrong number of arguments!")

    try:
        f = open(sys.argv[1], 'r')
    except IOError:
        print ("\nUsage: python sudoku_A2_xx.py input.txt output.txt\n")
        raise IOError("Input file not found!")

    puzzle = [[0 for i in range(9)] for j in range(9)]
    lines = f.readlines()

    i, j = 0, 0
    for line in lines:
        for number in line:
            if '0' <= number <= '9':
                puzzle[i][j] = int(number)
                j += 1
                if j == 9:
                    i += 1
                    j = 0

    sudoku = Sudoku(puzzle)
    ans = sudoku.solve()

    with open(sys.argv[2], 'a') as f:
        for i in range(9):
            for j in range(9):
                f.write(str(ans[i][j]) + " ")
            f.write("\n")
