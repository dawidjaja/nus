/* Lab 4  : Deadline 31st Otober 2020 (Sat) 11pm

   Generalizing Sudoku Puzzle in Prolog
   
   (revised sepcification on 18Oct2020)

   Below is a solution to Sudoku Puzzle
   can be found in SWI-Prolog web site. It is fun
   to see how it works for a 9 x 9 sudoku puzzle
   with 3 x 3 mini-blocks. If you have not played
   Sudoku before, please try mini-sudoku first:
      https://www.mathinenglish.com/puzzlessudoku.php

   There are variations of the suduko puzzle
   with different main grid sizes and mini-block sizes.

   For example, junior-sudoku is based on
       4 x 4 grid with 2 x 2 mini-blocks

   Another example is mini-sudoku that is based on
       6 x 6 grid with 3 x 2 mini-blocks

   Task 1 (80%)
   ======
   Generalize your sudoku solution generator using
   a new predicate gen_suduko below which supports
   different variations of sudoku puzzles, based on
   grid and mini-block sizes.

   gen_sudoku(Rows,N,B_R,B_C)
      N - size of entire block of N x N
      B_R - mini-block row size
      B_C - mini-block column size

   We can add the following constraints:
         N #>3, B_R >1, B_C>1, N #= B_R * B_C
   To restrict ourselves to regular-shaped sudokus that
   that are easier for humans to follow.
   
   The output for gen_sudoku will be made using maplist(portray_clause, Rows)
   in the query predicate.

   Task 2 (20%)
   ======
   Design a problem suduko generator, called
           find_puzzle_sudoku(Rows,S,N,M,B_R,B_C)
   that would generate a random sudoku puzzle of grid size S x S,
   mini-blocks B_R x B_C and which has from N to M known number of values. 

   Your solution may make use of random generator predicate
      random(+L:int, +U:int, -R:int)

   It should start with a random puzzle (whose numbers
   are well-spaced out) with N known numbers.
   If this did not return a unique solution, you could
   add one more (random) number to this incomplete puzzle,
   You can progressively do that until it hits M known numbers.

   If no unique puzzle is found with M known numbers, you can
   exit with a false outcome.

   PS You may use a predicate  aggregate_all(count, pred(X), Count). to count number of solutions.
   See https://stackoverflow.com/questions/6060268/prolog-count-the-number-of-times-a-predicate-is-true

   Due to the use of randomization, kindly note that the solution you get
   from this predicate is non-deterministic. You should try to
   think of some solutions that would give you the best possible outcome
   with smallest number of random values used,
   but without sacrificing too much on the time taken for your puzzle
   generator to terminate. If appropriate, you may repeat some of the 
   randomization processes but bearing in mind a trade-off between a 
   better solution versus time-out.
   
   We shall have a mini-competition to see who has the best find_puzzle_sudoku code. 
   For this mini-competition, the winner is one who can use the smallest number of
   known values used, followed by time taken. The competition is 
   just for fun and to encourage you to try your best.
   
*/

:- use_module(library(clpfd)).

/*
  Doc on maplist (similar to Haskell's map)
   https://www.swi-prolog.org/pldoc/man?predicate=maplist/2
  Doc on transpose/2 (for list of lists of same length)
   https://www.swi-prolog.org/pldoc/doc_for?object=clpfd%3Atranspose/2
  Doc on append/2 (to concatenate a list of lists)
   https://www.swi-prolog.org/pldoc/doc_for?object=append/2
  Doc on same_length/2 (to concatenate a list of lists)
   https://www.swi-prolog.org/pldoc/doc_for?object=same_length/2
  
*/


sudoku(Rows) :-
        length(Rows, 9), maplist(same_length(Rows), Rows),
        append(Rows, Vs), Vs ins 1..9,
        maplist(all_distinct, Rows),
        transpose(Rows, Columns),
        maplist(all_distinct, Columns),
        Rows = [As,Bs,Cs,Ds,Es,Fs,Gs,Hs,Is],
        blocks(As, Bs, Cs),
        blocks(Ds, Es, Fs),
        blocks(Gs, Hs, Is).

blocks([], [], []).
blocks([N1,N2,N3|Ns1], [N4,N5,N6|Ns2], [N7,N8,N9|Ns3]) :-
        all_distinct([N1,N2,N3,N4,N5,N6,N7,N8,N9]),
        blocks(Ns1, Ns2, Ns3).

problem(1, [[_,_,_,_,_,_,_,_,_],
            [_,_,_,_,_,3,_,8,5],
            [_,_,1,_,2,_,_,_,_],
            [_,_,_,5,_,7,_,_,_],
            [_,_,4,_,_,_,1,_,_],
            [_,9,_,_,_,_,_,_,_],
            [5,_,_,_,_,_,_,7,3],
            [_,_,2,_,1,_,_,_,_],
            [_,_,_,_,4,_,_,_,9]]).

problem(2, [[3,_,_,8,_,1,_,_,2],
            [2,_,1,_,3,_,6,_,4],
            [_,_,_,2,_,4,_,_,_],
            [8,_,9,_,_,_,1,_,6],
            [_,6,_,_,_,_,_,5,_],
            [7,_,2,_,_,_,4,_,9],
            [_,_,_,5,_,9,_,_,_],
            [9,_,4,_,8,_,7,_,5],
            [6,_,_,1,_,7,_,_,3]]).

mini_suduko(1,[[_,_,6,_,4,_],
               [_,_,_,_,6,_],
               [_,_,_,5,_,3],
               [3,_,_,_,_,_],
               [_,1,_,_,_,_],
               [_,5,_,_,4,_]]).

junior_suduko(1,[[_,4,_,1],
                 [3,_,4,_],
                 [1,_,_,4],
                 [_,2,1,_]]).

query(Prob,Rows) :- problem(Prob, Rows), sudoku(Rows), maplist(portray_clause, Rows).

gen_sudoku(Rows,N,B_R,B_C):- N #>1, B_R >1, B_C>1, N #= B_R * B_C.
/*
  N - size of entire block of N x N
  B_R - mini-block column size
  B_C - mini-block column size
*/

mini_sudoku(Rows) :- gen_sudoku(Rows,6,2,3).
junior_sudoku(Rows) :- gen_sudoku(Rows,4,2,2).
new_sudoku(Rows) :- gen_sudoku(Rows,9,3,3).

find_puzzle_sudoku(Rows,S,N,M,B_R,B_C):- N #>1, B_R >1, B_C>1, N #= B_R * B_C, 
		N #< M, S #= B_R * B_C.
/*
  puzzle generated with grid size S x S.
  the puzzle contain from N to M known values.
*/