-- Tutorial 1 : Introduction to Haskell
-- Use GHCi (ghci in command line); this is not intended to be compiled by ghc.
-- Execute the lines of expressions one by one
-- The purpose of this tutorial is to familiarized yourself with Haskell

"Hello to Lab1/Tutorial1"
"type ghci"
"type :script Tut1a.hs"
--------------------------------------------
-- PART 1 : This part to be attempted before tutorial
--------------------------------------------
-- Q1 (Scope). Consider the expression below

let x = 2

let y = let x=3 in x+1

let z = x+5

-- What are the values of x, y and z after the definition of z?
-- How many let definitions are there, and what are their scopes?

-- let x = x * 4 in x + y

-- Which x is being referenced in the last line?
-- What is the behavior of the last execution? Why?


---------------------------------------------
-- Q2 (Type). Consider the functions below

let goo1 x y = x
let goo2 x = \ y -> x
let goo3 (x,y) = x

-- What are the types of these three functions?
-- You can check the type of expression in ghci, say of goo1, by :type goo1 or :t goo1.
-- Are these types general?
-- Give some examples of calls to these functions?
-- Describe in words what are the intentions of these functions?
-- How are these functions related to each other?

---------------------------------------------
-- Q3 (Type). Consider the function below

let foo1 x = x + 1
let foo2 x = x ++ " there"

-- What are the types of these two function?
-- Give some examples of valid arguments that can
--  be used by foo1 and foo2 methods?
--  e.g foo1 2.5, foo1 4, foo1 "hello"
-- What is [Char]? Is this the same as String?
-- You may get same information about type t using :info t

---------------------------------------------
-- Q4 (Type). Consider this set of mutual recursive functions
-- Take note that multi-line commands are entered in ghci with
-- the help of commands :{ and  :}

:{
let even :: Int -> Bool
    even n = if n==0 then True else odd (n - 1)
    odd :: Int -> Bool
    odd n = if n==0 then False else even (n - 1)
:}

-- Illustrate some valid calls to these functions, such as odd 3, even 3
-- Explain what happens when you have negative inputs to these functions?
-- What are the types of these functions?
-- Explain what happens when type declarations of form e::t 
--   are not provided. 

------------------------------------------
-- Q5. Consider the maxlist function below for a non-empty list of numbers.
-- take note that

:{
let maxlist [] = error "empty list";
    maxlist (x:[]) = x;
    maxlist (x:ys) = max x (maxlist ys)
:}

maxlist [3,16,1]

-- (i) What is the type of this function?
-- (ii) Explain what happen when an empty list is supplied as the input?
-- (iii) Is it possible for recursive call, maxlist ys, in the third line
-- to throw an exception? Is it possible for ys to be []?

------------------------------------------
-- Q6. Rewrite maxlist function to use an auxiliary recursive
--     function, as follows:

:{
let maxlist2 xs =
     let aux xs sofar = 
          error "TBI - to be implemented"
     in
     case xs of
     [] -> error "empty list"
     (x:ys) -> aux ys x
:}

-- maxlist2 [3,16,1]

------------------------------------------------------------------
-- PART 2 : Presentation onf PL Concepts by Groups of 2 or 3
--------------------------------------------------------------------
{-
  Please form into a small group of 2 (or 3) to prepare a presentastion
  on a selected topic. Each group should prepare a set of slides on 
  fordiscussion at least FOUR times over the course of
  the semester. The slides should be deposited into workbin prior to
  the actual tutorial session. Each week about 4-6 groups may be called 
  out for 5-10 mins presentation during the tutorial session. PLease limit
  your presentation time strictly to 10mins. The recording will be 
  kept for reviewing by the class. If needed, supplemenatry sessions will be organized
  to allow all groups to present and share their findings on the 
  PL concept topics that they have investigated. Students may compare
  similar features across two or more programming langauges.
  
  Below are two topics for you to choose in Week 3. We will allow up to 6 groups to
  select these two topics. The others may select topics on the following weeks.
  
  Topic 1a
  ========
  What is the "layout rule" syntactic mechanism in programming language?
  Why is this called a two-dimentional syntax mechanism?
  Why would you want to design layout rule mechamism for a new PL?
  How are layout rules normally implemented?
  What other PL uses layout rule mechanism? Can you 
  compare and contrast their features?
  
  Topic 1b
  ========
  What are "binders" in programming language?
  Why do you need binders?
  How are binders normally implemented?
  Compare and contrast the binder concepts in another
  interesting programming languages that you know 
  in comparison with Haskell.
   
-}

-----------------------------------------------------
-- PART 3 : This part for small discussions and problem solving.
-- If we do not have time to organize, you should schedule another
-- separate sessions with fellow students for some discussions over the problems.
------------------------------------------------------

-------------------------------------------
-- Q7 : The maxlist function currently has type: [Int] -> Int.
--      Rewrite it to a version that is based on the following Maybe type.
--      data Maybe a = Nothing | Just a

:{
let maxlist3 xs =
     case xs of
     [] -> Nothing;
     (x:[]) -> Just x;
     (x:ys) -> error "TBI - to be implemented"
:}

-- The function 'show' converts a number into a string or [Char]
-- The function 'print' outputs any thing (almost) to the standard output
-- The function (++) concatenates two strings

-- print ("Q7a Ans (maxlist3 [3,16,1] = " ++ (show (maxlist3 [3,16,1])))
-- print ("Q7a Ans (maxlist3 [3,16,1] = " ++ (show (maxlist3 [])))

-- How is this function different from the version in Q6
-- Is it better?

:{
let maxlist4 xs =
     let aux xs sofar =
          error "TBI - to be implemented"
     in
     case xs of
     [] -> Nothing;
     (x:ys) -> Just (aux ys x)
:}

-- print ("Q7b Ans (maxlist4 [3,16,1] = " ++ (show (maxlist4 [3,16,1])))
-- print ("Q7b Ans (maxlist4 [3,16,1] = " ++ (show (maxlist4 [])))

-------------------------------------------
-- Q8 : Using function for abstraction.
-- Consider a simple code to compute the sum of 
-- the areas for three circles.
--
-- How would could you generalize the code 
-- to make it more abstract
-- so that it could compute the sum for an
-- arbitrary number of circules instead.
-- Hint : Use a list & use functions!

let pi = 3.14159
let r1 = 1.2 
let r2 = 3.888
let r3 = 10.0
let three_circle = 2*pi*r1 + 2*pi*r2 * 2*pi*r3

-------------------------------------------
-- Q9 : Implement the Babylonian method for square root in
-- Haskell without using loops! Please use a recursive method
-- instead. What would be its expected type?

:{
{-
Babylonian method for square root
Algorithm:
This method can be derived from (but predates) Newton–Raphson method.

1. Start with an arbitrary positive start value x (the closer to the 
   root, the better).
2. Initialize y = 1.
3. Do following until desired approximation is achieved.
   a) Get the next approximation for root using average of x and y
   b) Set y = n/x

Implementation in C:
/*Returns the square root of n. Note that the function */
float squareRoot(float n)
{
  /*We are using n itself as initial approximation
   This can definitely be improved */
  float x = n;
  float y = 1;
  float e = 0.000001; /* e decides the accuracy level*/
  while(x - y > e)
  {
    x = (x + y)/2;
    y = n/x;
  }
  return x;
}
/* Driver program to test above function*/
int main()
{
  int n = 50;
  printf ("Square root of %d is %f", n, squareRoot(n));
  getchar();
}
Running on IDE
Example:
n = 4 /*n itself is used for initial approximation*/
Initialize x = 4, y = 1
Next Approximation x = (x + y)/2 (= 2.500000), 
y = n/x  (=1.600000)
Next Approximation x = 2.050000,
y = 1.951220
Next Approximation x = 2.000610,
y = 1.999390
Next Approximation x = 2.000000, 
y = 2.000000
Terminate as (x - y) > e now.

Write a set of similar functions in Haskell
-}
  :}
