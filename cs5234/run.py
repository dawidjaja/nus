import random
import time
import statistics

N = 10**6 # number of element
M = 25 # number of unique element
A = 10**3 # hash function number
B = 10 # hash function number of bucket
p = 10**10 # hash randomizer

freq = {}
C = {}

arr_a = []
arr_b = []


def f(i, x):
    return ((arr_a[i]*x + arr_b[i]) % p) % B

def add(x):
    freq[x] += 1
    for i in range(A):
        tup = (i, f(i, x))
        if tup in C:
            C[tup] += 1
        else:
            C[tup] = 1

def gen(dataset):
    
    for i in range(M):
        freq[i] = 0

    for i in range(A):
        arr_a.append(random.randint(1, p - 1))
        arr_b.append(random.randint(0, p - 1))

    if (dataset == 0):
        for i in range(N):
            x = random.randint(0, M - 1)
            add(x)
    
    elif (dataset == 1):
        for i in range(N):
            x = 0
            while (random.randint(0, 1) == 0 and x < M - 1):
                x += 1
            add(x)

    elif (dataset == 2):

        # opening the text file
        with open('shakespeare.txt.txt','r') as file:
            # reading each line    
            for line in file:
                # reading each word        
                for word in line.split():
                    # displaying the words           
                    add(word)

def check_ans():
    res_algo1 = {}
    res_algo2 = {}

    # algo 1
    for x in range(M):
        arr = []
        for i in range(A):
            arr.append(C.get((i, f(i, x)), 0))
        print(arr)
        res_algo1[x] = statistics.median(arr)

    # algo 2
    for x in range(M):
        arr = []
        for i in range(A):
            h = f(i, x)
            cur = C.get((i, h), 0)
            neigh = 0
            if (h % 1 == 0):
                neigh = h + 1
            else:
                neigh = h - 1
            est = cur - C.get((i, neigh), 0)
            arr.append(est)

        print(arr)
        res_algo2[x] = statistics.median(arr)

    tot = [0, 0]
    for i in range(M):
        a = freq[i]
        b = res_algo1[i]
        c = res_algo2[i]
        print("data", i, "--> real result = ", freq[i])
        tot[0] += abs(a - b)
        tot[1] += abs(a - c)

        print("  algo1 =", res_algo1[i], "diff =", abs(a-b))
        print("  algo2 =", res_algo2[i], "diff =", abs(a-c))
        print()

    print(tot[0] / N, "---", tot[1] / N)



def init():

    gen(1)

    for i in range(M):
        print(i, "-->", freq[i])

    check_ans()
    print("N =", N)
    print("M =", M)
    print("A =", A)
    print("B =", B)

def main():

    random.seed(time.time())
    init()

if __name__ == '__main__':
    main()
