fact = [1]
invfact = [1]
last = 1;
MOD = (int)(1e9 + 7);
n = int(input())
for i in range(1, n):
    last *= i
    last = last % MOD
    fact.append(last)
    invfact.append(pow(last, MOD - 2, MOD))
print(fact)
print(invfact)
