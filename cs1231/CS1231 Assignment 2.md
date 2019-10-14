# Assignment 2 

Daniel Alfred Widjaja

E0313577

TG30

### Question 1

#### 	1.a

$$
\begin{align}
&=(R\circ R)(aba)\\
&=(R)(R(aba))\\
&=R(R(aba))\\
&=R(bba)\\
&=(bbb)
\end{align}
$$

#### 	1.b

$$
\begin{align}
&\text{No}\\
&R(ab) = R(bb) = bb\\
\end{align}
$$

#### 	1.c

$$
\begin{align}
&\text{Yes}\\
&\text{By definition C is onto iff $\forall y \in \N\space \exist x\space C(x) = y$}\\
&x\text{ is a string of length $y$ consist of only '$a$' character}\\
&\text{QED}
\end{align}
$$





### Question 2

####		2.a

$$
\begin{align}
&\text{If there is only $1$ dice}\\
&\text{Expected value = $(\$2 * (2 + 3 + 5) - \$3 * (1 + 4 + 6)) / 6$}\\
&\text{Expected value = $-\$\frac{11}{6}$}\\\\
&\text{If there is 100 die}\\
&\text{Expected value = $100 * (-\$\frac{11}{6})$}\\
&\text{Expected value = $-\$183.\overline{3}$}\\
&\text{Expected value = $-\$183$}\\
\end{align}
$$



#### 		2.b

$$
\begin{align}
\mathop{\mathbb{E}} &= 20 + 10 * (\frac{1}{3} + (-\frac{1}{4} * \frac{2}{3}))\\
\mathop{\mathbb{E}} &= 20 + 10 * (\frac{1}{3} + (-\frac{2}{12}))\\
\mathop{\mathbb{E}} &= 20 + 10 * (\frac{1}{6})\\
\mathop{\mathbb{E}} &= 20 + (\frac{5}{3})\\
\mathop{\mathbb{E}} &= 21.\overline{6}\\
\end{align}
$$



### Question 3

$$
\begin{align}
\mathbb{P}(A) &= 1 - \mathbb{P}(\neg{A})\\
\mathbb{P}(A) &= 1 - (\frac{5}{6})^6\\
\mathbb{P}(A) &= 0.6651\\
\\
\mathbb{P}(B) &= 1 - \mathbb{P}(\neg{B})\\
\mathbb{P}(B) &= 1 - ((\frac{5}{6})^{12} + \frac{1}{6}\cdot(\frac{5}{6})^{11}\cdot{12\choose1})\\
\mathbb{P}(B) &= 0.6186\\
\\
\mathbb{P}(C) &= 1 - \mathbb{P}(\neg{C})\\
\mathbb{P}(C) &= 1 - ((\frac{5}{6})^{18} + \frac{1}{6}\cdot(\frac{5}{6})^{17}\cdot{18\choose1} + \frac{1}{36}\cdot(\frac{5}{6})^{16}\cdot{18\choose2})\\
\mathbb{P}(C) &= 0.5973\\
\therefore \mathbb{P}(A) &\text{ has the highest probability of winning}
\end{align}
$$

### Question 4

$$
\begin{align}
&\text{Let $A, B, C$ be the vertices of the triangle.}\\
&\text{Let $D, E, F$ be the $midPoint$ of $\overline{AB}, \overline{BC}, \overline{AC}$ respectively.}\\
&\text{There will be 4 new smaller equilateral triangle with side of 5 cm which is $\triangle ADF, \triangle BDE, \triangle CEF, $ and $\triangle DEF$.}\\
&\text{By PHP, with these smaller triangle as the holes and the dots as the pigeon, it's guaranteed that at least one triangle have at least 2 dots.}
\end{align}
$$

### Question 5

#### 	5.a 

$$
\begin{align}
&=\text{$\mathop{\mathbb{P}}$($\neg $rain $\cap$ heavyTrafic $\cap$ $\neg$late)}\\
&=\text{$\mathop{\mathbb{P}}$($\neg $rain) $\cdot$ $\mathop{\mathbb{P}}$(heavyTraffic $\mid \neg $rain) $\cdot$ $\mathop{\mathbb{P}}$($\neg$late $\mid$ (heavyTraffic $\cap \neg $rain))}\\
&=\frac{1}{4} \cdot \frac{1}{3} \cdot \frac{1}{2}\\
&=\frac{1}{24}
\end{align}
$$

#### 	5.b

$$
\begin{align}
&=\mathbb{P}(late)\\
&=\mathbb{P}(late\mid heavyTraffic) + \mathbb{P}(late\mid \neg heavyTraffic)\\
&=\mathbb{P}(late\mid heavyTraffic\mid rain) + \mathbb{P}(late\mid heavyTraffic\mid \neg rain) + \mathbb{P}(late\mid \neg heavyTraffic\mid rain) + \mathbb{P}(late\mid \neg heavyTraffic\mid \neg rain)\\
&=\frac{1}{2}\cdot\frac{1}{3}\cdot\frac{1}{4}+\frac{1}{4}\cdot\frac{1}{5}\cdot\frac{3}{4}+\frac{1}{4}\cdot\frac{4}{5}\cdot\frac{1}{4}+\frac{1}{10}\cdot\frac{2}{3}\cdot\frac{3}{4}\\
&=\frac{1}{24} + \frac{3}{80} + \frac{1}{20} + \frac{1}{20}\\
&=\frac{43}{240}
\end{align}
$$

#### 	5.c

$$
\begin{align}
&=\mathbb{P}(rain | late)\\
&=\frac{\mathbb{P}(rain \cap late)}{\mathbb{P}(rain)}\\
&=\frac{\mathbb{P}(rain \cap late\cap heavyTraffic)+\mathbb{P}(rain \cap late\cap\neg heavyTraffic)}{\mathbb{P}(rain)}\\
&=\frac{\frac{1}{4}\cdot(\frac{1}{3}\cdot\frac{1}{2} +\frac{2}{3}\cdot\frac{1}{5})}{\frac{1}{4}}\\
&=\frac{3}{10}
\end{align}
$$

