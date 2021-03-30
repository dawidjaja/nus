father(john, mary).
father(john, tom).
father(kevin, john).
mother(eva, mary).
mother(eva, tom).
mother(cristina, john).
mother(cristina, kelly).
mother(kelly, alloy).
male(john).
male(kevin).
male(tom).
female(X):-atom(X),not(male(X)). /* works for known X */
parent(X,Y) :- father(X,Y).
parent(X,Y) :- mother(X,Y).
grandparent(X,Y) :- parent(X,Z),parent(Z,Y).
daughter(X,Y) :- female(X),parent(Y,X).
sibling(X,Y) :- parent(Z,X),parent(Z,Y),X\==Y.
true_sibling(X,Y) :- father(Z,X),father(Z,Y),X\==Y,mother(M,X),mother(M,Y).
ancestor(X,Y) :- parent(X,Y).
ancestor(X,Y) :- parent(X,Z),ancestor(Z,Y).

poo([],0).
poo([_|Xs],S) :- poo(Xs,R), S is 1+R.

beep([1]).
beep([1]).

/* Q1 */
/* Q1. (Answer and explanation)
grandparent(G,mary).
G = kevin ;             % parent(kevin, john), parent(john, mary). 
G = cristina ;          % parent(cristina, john), parent(john, mary).
false.

ancestor(A,mary).
A = john ;              % parent(john, mary).
A = eva ;               % parent(eva, mary). 
A = kevin ;             % parent(kevin, john), ancestor(john, mary).
A = cristina ;          % parent(cristina, john), ancestor(john, mary).
false.

sibling(S,mary).        
S = tom ;               % parent(john, tom), parent(john, mary).
S = tom ;               % parent(eva, tom), parent(eva, mary).

*/

/* Q2
   aunt(X,Y) means X is aunt of Y
   cousin(X,Y) means X is cousin of Y
   nephew(X,Y) means X is nephew of Y
 Many possible answers since conjunction is
 in general commutative.
 Below is one of the possible answers.
*/
aunt(X,Y) :- sibling(X,Z), parent(Z,Y), female(X).
/*
  female(X) needs to be put last to allow its parameter
  to be instantiated due to use of negation as failure not(..)
*/
cousin(X,Y) :- parent(Z,X), sibling(Z,W), parent(W,Y).
nephew(X,Y) :- male(X), parent(Z,X), sibling(Z,Y).


sel(X,[X|_]).
sel(X,[_|T]) :- sel(X,T).
remDupl([],[]).
remDupl([H|T],R) :- sel(H,T), remDupl(T,R).
remDupl([H|T],[H|R]) :- remDupl(T,R).


append([],Y,Y).
append([X|Xs],Y,[X|Rs]):-append(Xs,Y,Rs).
rev([],[]).
rev([X|Xs],Y) :- rev(Xs,Y2),append(Y2,[X],Y).
rev3([],[]).
rev3([X|Xs],Y) :- append(Y2,[X],Y),rev3(Xs,Y2).
rev2([],[]).
rev2(.(X,Xs),Y) :- rev2(Xs,Y2),append(Y2,[X],Y).
fact(0,1).
fact(N,R) :- N>0, M is N-1, fact(M,R1), R is N*R1.
select2(X,[X]).
select2(X,[_|T]):-select2(X,T).

/* Q3
 last(Xs,X) :- X is the last element of Xs
 len(Xs,N) - N is the length of Xs
 nth(Xs,I,X) - X is the I-th element of Xs, starting from 0.
 occurs(Ys,X,N) - X occurs N-times in Ys.
 You may use the finite constraint solving package.

   last([X]) ==> X
   last([X|T]) ==> last(T)
*/

coo([], []).
coo([[]|Y], X):- coo(Y, X).
coo([[X|A]|Y], [X|Z]):- coo([A|Y], Z).

sharl([]).
sharl([X, X|Xs]) :- sharl(Xs).

toto([], 0).
toto([[]|Y], X):- toto(Y, X).
toto([[X|A]|Y], S) :- S #= T+X, toto([A|Y], T).

last([X], X):-!.
last([_|Y], X) :- last(Y, X).

/* Below assumes N is the output */
len([], 0).
len([_|Y], N) :- len(Y, M), N is M+1.
/* A more general len predicate can be
   designed as follows to allow N to be used
   as output in len1, and N to be used as
   input in len2.

len1([],0).
len1([_|Xs],N):-len1(Xs,M),M>=0,N is M+1.
len2([],0).
len2([X|Xs],N):-N >0, M is N-1,len2(Xs,M).
len(Xs,N):-integer(N),!,len2(Xs,N).
len(Xs,N):-len1(Xs,N).
*/

test([(X, Z)|Y], X, Y, Z).

nth([X|_], 0, X) :- !.
nth([_|Y], N, Z) :- N > 0, M is N-1, nth(Y, M, Z).

occurs([], _, 0) :- !.
occurs([X|Y], X, N) :- occurs(Y, X, M), N is M+1, !.
occurs([X|Y], Z, N) :- X \= Z, occurs(Y, Z, N). 


/* Q5
   Hint: you may use mod operation
   ?- divisors(30,X).
	X = [1,2,3,5,6,10,15,30]
*/

divides(X, Y) :- Y > 0, 0 is X mod Y.
divisors1(_, 0, []) :- !.
divisors1(X, N, [N|L]):- N>0, P is N-1, divides(X,N), divisors1(X,P,L), !.
divisors1(X, N, L):- N>0, P is N-1, not(divides(X,N)), divisors1(X,P,L).

divisors(X, L) :- X > 0, divisors1(X, X, L).

/*
  Here are the definitions of similar functions using constraint logic programming finite domain.
  Notice that these definitions are more powerful than the definitions given above.
  e.g. nth([1,4,10,5,4,8,10,7,8,10],X,8) does not provide us the positions of 8 in the list,
  however, cnth([1,4,10,5,4,8,10,7,8,10],X,8) provides us the positions of 8, 5th and 8th.
  You can try similar example to see noccurs is more powerful than occurs.
*/


:- use_module(library(clpfd)).

clen([], 0).
clen([_|Y], N) :- clen(Y, M), N #= M+1.

cnth([X|_], 0, X).
cnth([_|Y], N, Z) :- N #> 0, N #= M+1, cnth(Y, M, Z).

coccurs([], _, 0).
coccurs([X|Y], X, N) :- N #= M+1, coccurs(Y, X, M).
coccurs([X|Y], Z, N) :- X #\= Z, coccurs(Y, Z, N). 








