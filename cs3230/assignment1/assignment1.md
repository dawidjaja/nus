1. $$
   \begin{align*}
   &g_{1}(n) = \Theta(n!)\\
   &g_{2}(n) = \Theta(n^3\lg n)\\
   &g_{3}(n) = \Theta(n^{\lg n})\\
   &g_{4}(n) = \Theta(\sqrt{n})\\
   &g_{5}(n) = \Theta(2^{\lg n})\\
   &g_{6}(n) = \Theta(\lg ^3n)\\
   &g_{7}(n) = \Theta(2 ^ n)\\
   &g_{8}(n) = \Theta(n^3\lg\lg n)\\
   g_6 << g_4 << g_5 <&< g_8 << g_2 << g_3 << g_7 << g_1
   \end{align*}
   $$


--------------

2. 

   a. 
   $$
   \begin{align}
   T(n) &= 4\cdot T(n/2) + \Theta(n^2\lg^3 n)\\
   a &= 4\\
   b &= 2\\
   f(n) &= \Theta(n^2\lg^3n)\\
   n^{\log_ba} &= n^{\log_24}\\
   &= n^2\\
   Case2:\\
   f(n) &= \Theta(n^{log_24} \lg^1 n)\\
   T(n) &= \Theta(n^2\lg^2 n)
   \end{align}
   $$

   1. $$
      T(n) = 3T(n/2) + 5n
      $$


------




$$
\begin{align*}
U(n)&=\frac{2}{n-1}\sum_{k=1}^{n-1}U(k) + 5n\\
U(n+1)&=\frac{2}{n-1}\sum_{k=1}^{n-1}U(k) + 5n\\
U(n+1)&=\frac{2}{n-1}U(n-1) + 10 + \frac{2}{n-1}\sum_{k=1}^{n-2}U(k) + 5(n-2)\\
U(n+1)&=\frac{2}{n-1}U(n-1) + \frac{n-2}{n-1}U(n-1) + 10\\
U(n+1)&=(\frac{2}{n-1} + \frac{n-2}{n-1})U(n-1) + 10\\
U(n+1)&=(\frac{2}{n} + \frac{n-1}{n})U(n) + 10\\
U(n+1)&=(\frac{n+1}{n})U(n) + 10\\
U(n+1)&=(\frac{n+1}{n})((\frac{n}{n-1})U(n-1) +10)+10\\
U(n+1)&=n + 1 + 10(1 + \frac{n+1}{n}+\frac{n+1}{n-1}+\dots+\frac{n+1}{1})\\
U(n+1)&=n+1+10(1+(n+1)*\sum_{x=1}^{n}\frac{1}{x})\\
U(n+1)&=n+1+10(1+(n+1)*\ln n)\\
U(n+1)&=n+1 + 10 + 10n\ln n + 10\ln n\\
U(n+1)&=O(n \log n)

\end{align*}
$$








3. 







