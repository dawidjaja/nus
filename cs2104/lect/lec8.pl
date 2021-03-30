father(john, mary).
father(john, tom).
father(kevin, john).
mother(eva, mary).
mother(eva, tom).
mother(cristina, john).
male(john).
male(kevin).
male(tom).
female(X):-not(male(X)).
female(cristina).
female(mary).
parent(X,Y) :- father(X,Y).
parent(X,Y) :- mother(X,Y).
grandparent(X,Y) :- parent(X,Z),parent(Z,Y).
daughter(X,Y) :- female(X),parent(Y,X).
sibling(X,Y) :- parent(Z,X),parent(Z,Y),X\==Y.
ancestor(X,Y) :- parent(X,Y).
/* ancestor(X,Y) :- parent(X,Z),ancestor(Z,Y). */
ancestor(X,Y) :- ancestor(Z,Y),parent(X,Z).

/* Q1 */
append([],Y,Y).
append([X|Xs],Y,[X|Rs]):-append(Xs,Y,Rs).
rev([],[]).
rev([X|Xs],Y) :- rev(Xs,Y2),append(Y2,[X],Y).
rev2([],[]).
rev2(.(X,Xs),Y) :- rev2(Xs,Y2),append(Y2,[X],Y).
fact(0,1).
fact(N,R) :- N>0, M is N-1, fact(M,R1), R is N*R1.
:- use_module(library(clpfd)).
cfact(0, 1).
cfact(N, R) :- N #> 0, N1 #= N - 1, R #= N * R1, cfact(N1, R1).
select2(X,[X]).
select2(X,[_|T]):-select2(X,T).

puzzle([S,E,N,D] + [M,O,R,E] = [M,O,N,E,Y]) :-
        Vars = [S,E,N,D,M,O,R,Y],
        Vars ins 0..9,
        all_different(Vars),
                  S*1000 + E*100 + N*10 + D +
                  M*1000 + O*100 + R*10 + E #=
        M*10000 + O*1000 + N*100 + E*10 + Y,
        M #\= 0,label([S,E,N,D]).


