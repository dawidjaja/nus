# CS1231 Assignment 1

### Daniel Alfred Widjaja

#### A0184588J

### Tutorial Group 30

## Problem 1

### 1.a.

$\forall x(\forall y(Loves(x, \text{santa}) \and Reindeer(y) \rightarrow Loves(x, y)))$ 

### 1.b.

$\exist!x (Loves(x, Mary) \and Loves(\text{John}, \text{Mary}))$ 

### 1.c.

$\forall x\forall y(Reindeer(y) \and \neg \exist z(Reindeer(z) \and Loves(x, y) \and Loves (x, z)))$ 

## Problem 2

### 2.a

1. $\forall x(Musician(x) \rightarrow \neg Singer(x))$ 
2. $\exist x(Singer(x) \rightarrow Dancer(x))$ 

### 2.b

1. $\forall x(Actor(x) \rightarrow Musician)$ 

2. $\forall x(Musician(x) \rightarrow \neg Singer(x))$ 

3. $\forall x(Actor(x) \rightarrow \neg Singer(x))$ (*by Law of Transitivity*)

   $\therefore \text {Conclusion 3 is }true$ 

## Problem 3

1. $a \or b$ *(W1)*
2. $\neg a$ *(W2)* 
3. If a chest contain cobra, then it won't contain treasure *(C1)*
4. $W1 \oplus \neg W2$ *(C2)*
5. $\text{If W1}$  
   1. $\text{W2 is }true$ *(by C2)* 
   2. $\neg a$ (by W2)
   3. $b​$ *(by W1 and W2)*
6. $\text{If }\neg \text{W1}\ (\neg a \and \neg b)$ *(by De Morgan's Law)*
   1. $\neg \text{W2}$ *(C2)* $(a)$ *by conjunction intro)*
   2. $a \and \neg a \and \neg b$ 
   3.  $false \and \neg b$ *(by negation law)*
   4. $false$ *(by Identity law)*
   5. This case won't happen.
7. It is guaranteed to find treasure in chest B (5.3).

## Problem 4 

### 4.a

1. $\text{Let }n\text{ be represent of the form }d_{k}d_{k-1}\dots d_{2}d_{1}d_{0}$ 
2. $n = d_{k}\cdot 10^{k} + d_{k - 1}\cdot 10 ^{k - 1} + \dots + d_2 \cdot 10^2 + d_1 \cdot 10 ^1 + d_0 \cdot 10 ^ 0 $  
3. *Proof (by Direct Proof)*.
4. $SumDigits(n) \equiv n\ (mod\ 9)$ 
5. $d_k + d_{k - 1} + \dots + d_2 + d_1 + d_0 \equiv d_{k}\cdot 10^{k} + d_{k - 1}\cdot 10 ^{k - 1} + \dots + d_2 \cdot 10^2 + d_1 \cdot 10 ^1 + d_0 \cdot 10 ^ 0 \ (mod\ 9)$ 
6. $d_k + d_{k - 1} + \dots + d_2 + d_1 + d_0 \equiv d_{k}\cdot 1^{k} + d_{k - 1}\cdot 1 ^{k - 1} + \dots + d_2 \cdot 1^2 + d_1 \cdot 1 ^1 + d_0 \cdot 1 ^ 0 \ (mod\ 9)$ *(based on Theorem Corollary 8.4.4 (Epp))*  
7. $d_k + d_{k - 1} + \dots + d_2 + d_1 + d_0 \equiv d_{k} + d_{k - 1} + \dots + d_2 + d_1 + d_0 \ (mod\ 9)$
8. $\therefore SumDigits(n) \equiv n\ (mod\ 9)$ 

### 4.b

1. $SumDigits(3m) \leq SumDigits(m) \cdot SumDigits(3)$ *(by P3)*
2. $SumDigits(3m) \leq 100 \cdot 3$
3. $SumDigits(3m) \leq 300$ 
4. $SumDigits(44m) \leq SumDigits(41m) + SumDigits(3m)$ *(by P2)*
5. $SumDigits(44m) \leq SumDigits(41) \cdot SumDigits(m) + SumDigits(3m)$ *(by P3)*
6. $SumDigits(44m) - SumDigits(41) \cdot SumDigits(m) \leq SumDigits(3m)$ *(by basic algebra)*
7. $800 - 5 \cdot 100 \leq SumDigits(3m)$
8. $300 \leq SumDigits(3m)$
9. $300 \leq SumDigits(3m) \leq 300$
10. $\therefore SumDigits(3m) = 300$ *(by basic algebra)*



## Problem 5

1. *Proof (by Induction):*
   1. *Base Case: $P(1)$*
   2. There will be only one configuration which is red-blue (blue-red is the same because it form a circle)
   3. Choose the red ball so it will be *successful* trip.
   4. *Induction Hypothesis $P(k) \text{ for } \forall k \in \mathbb{Z}(k > 1)$ *:
   5. Let the next ball of a ball is the ball that next to it in clockwise direction.
   6. There must be exist a red ball that have a blue ball as its next ball. 
      1. *Proof (by Contradiction)*:
      2. For all red ball, it doesn't have a blue ball as its next ball. *(the negation)*
      3. The only case 6.2 will happen if all the ball is red color. 
      4. 6.3 won't happen because there are n number of red ball and n number of blue ball.
      5. The contradiction is false. Therefore the initial statement is true.
   7. If we delete those 2 balls, we get $P(k - 1)$. 
   8. So the ball that we choose in $P(k - 1)$ is also become the ball we choose in $P(k)$ 
   9. Let $x$ is the red ball we remove in $7$.
   10. Let $r$ is the amount of red ball until the ball before $x$.
   11. Let $s$ is the amount of blue ball until the ball before $x$.
   12. Because $P(k - 1)$ is successful, therefore,  $r\geq s$. 
   13. At the ball $x, r +1 \geq s$
   14. And at the ball after $x, r + 1 \geq s + 1$
   15. For the balls after that, the amount of red ball always greater than the amount of blue balls since the amount of both balls at $P(k)$ is 1 greater than the one in $P(k-1)$.
   16. $\therefore P(k - 1) \rightarrow P(k)$. 