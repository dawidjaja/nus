module Lab01 where

import Prelude hiding (last)
import Control.Exception (assert)

{-
Assignment 1 
============
Please complete and submit a single file containing your
solution as a Haskell file. Any textual answers shoudld
be embedded using:

{- ANSWER
   .....
-}

Deadline : Please submit by 12 Sept 2020 (9pm Sat) on LumiNUS.
Late assignments will be penalised. 10% per day upto a max
of 5-days.
-}

{-|
  Q1:

  Write a recursive function that would return the last element of a list,
  if it exists. Also, give its polymorphic type signature.

  If the last element of the list is v, then it would return 'Just v'.
  If the list is empty, it should return 'Nothing'.
-}
--last xs = error "To be implemented"

last :: [a] -> Maybe a

last [] = Nothing
last (x:[]) = Just x
last (x:xs) = last xs


{-|
  Q2:

  Write a recursive function that would return the last two elements
  elements of a list, if it exist.

  If the last two elements of the list are [..,x,y], then it would return
  'Just (x,y)'.
  If the list has less than 2 elements, it should return 'Nothing'.
-}
--lastSnd xs = error "To be implemented"

lastSnd [] = Nothing
lastSnd (x:[]) = Nothing
lastSnd (x1:x2:xs) = case xs of 
  [] -> Just (x1,x2)
  _ -> lastSnd (x2:xs)

{-|
  Q3:

  Insertion sort is a sorting algorithm that sorts a list by putting each
  element of a list into its correct sorted position.

  (i) Implement a recursive sort function. For your convenience, we have
      provided an 'insert' function.

  (ii) Can you improve the insert function to avoid having to construct 'y : ys'
       again in the recursive case? (Hint: Use Haskell's as-patterns)
-}
insert :: Int -> [Int] -> [Int]
insert x [] = [x]
insert x (y : ys) =
  if x < y then
    x : y : ys
  else
    y : insert x ys

sort :: [Int] -> [Int]
--sort xs = error "To be implemented"
sort xs = 
  case xs of
  [] -> []
  (x:[]) -> insert x []
  (x:ys) -> insert x (sort ys)

{-|
  Q4:

  Consider an Uprim type to capture either an integer, a float, or a string
  value.

  We can then build a mixed list of integers, floats and strings, and can
  compute the reverse and length of such a list.

  Compute the sum of a mixed list using the 'valueOfMix' function.
-}

data Uprim
  = I Integer
  | F Float
  | S String
  deriving Show

mixList :: [Uprim]
mixList = [I 3, F 4.3, S "hello", I 4]

valueOfMix :: Uprim -> Int
valueOfMix up =
  case up of
    I i -> fromInteger i
    F f -> floor f
    S s -> length s

sumOfMixList :: [Uprim] -> Int
--sumOfMixList ms = error "To be implemented"
sumOfMixList [] = 0
sumOfMixList (m:ms) = (valueOfMix m) + (sumOfMixList ms)

{-|
  Q5:

  Let us define Uprim using the basic Sum type instead, and write functions that
  are isomorphic to those found in Q4.

  Compute the sum of a mixed list using the 'valueOfMix2' function.
-}
data Sum a b
  = L a
  | R b
  deriving Show

type Uprim2 = Sum Integer (Sum Float String)

makeI :: Integer -> Uprim2
makeI = L

makeF :: Float -> Uprim2
makeF = R . L

makeS :: String -> Uprim2
makeS = R . R

mixList2 :: [Uprim2]
mixList2 = [makeI 3, makeF 4.3, makeS "hello", makeI 4]

valueOfMix2 :: Uprim2 -> Int
valueOfMix2 up =
  case up of
    L i -> fromInteger i
    R (L f) -> floor f
    R (R s) -> length s

sumOfMixList2 :: [Uprim2] -> Int
sumOfMixList2 [] = 0
sumOfMixList2 (m:ms) = (valueOfMix2 m) + (sumOfMixList2 ms)

{-|
  Q6:
  Consider a polymorphic binary tree type.
  Write a function that will return the smallest value in the tree. You may use
  the 'min' function.
  
  Since we may have Empty tree, we need to implement minTree with Maybe type, namely
  minTree :: BTree Int -> Maybe Int. Using exceptions will be more challenging
  in Haskell since exceptions are captured via Monad class. 
-}

data BTree a
  = Empty
  | Leaf a
  | Node a (BTree a) (BTree a)

tree1 :: BTree Int
tree1 = Leaf 3

tree2 :: BTree Int
tree2 = Node 4 tree1 tree1

tree3 :: BTree Int
tree3 = Node 6 tree2 tree1

minTree :: BTree Int -> Maybe Int
minTree tree = 
  case tree of
    Empty -> Nothing
    Leaf v -> Just v
    Node v t1 t2 -> case (v, minTree t1, minTree t2) of
      (v, Nothing, Nothing) -> Just v
      (v, Just a, Nothing) -> Just (min v a)
      (v, Nothing, Just b) -> Just (min v b)
      (v, Just a, Just b) -> Just (min (min a b) v)


{-|
  Q7:

  'flattenInfix' is a function that will flatten a tree into a list by
  traversing it in infix order.

  Write another function that will flatten a tree by traversing it in postfix
  order. In postfix order, the left and right sub-trees are traversed before
  the node.
-}
flattenInfix :: BTree a -> [a]
flattenInfix Empty = []
flattenInfix (Leaf v) = [v]
flattenInfix (Node v leftTree rightTree) =
  flattenInfix leftTree ++ [v] ++ flattenInfix rightTree

flattenPostfix :: BTree a -> [a]
flattenPostfix Empty = []
flattenPostfix (Leaf v) = [v]
flattenPostfix (Node v leftTree rightTree) = 
  flattenPostfix leftTree ++ flattenPostfix rightTree ++ [v]

{-|
  Q8:

  The 'power' function takes 2 arguments 'x' and 'n' and returns x to the nth
  power, e.g. power 2 3 = 8.

  The current implementation implicitly assumes that n >= 0.

  (i) What happens when a negative value of n is used?
  (ii) Add an assertion to the function to ensure that the implicit assumption
       is always met. Make use of the 'assert' function imported at the
       beginning of this file.

       The 'assert' function has type Bool -> a -> a.
-}
power :: Int -> Int -> Int
power x n =
  if (assert (n >= 0) n) == 0 then
    1
  else
    x * power x (n - 1)

{-|
  Q9:

  'power' merely expresses the fact that
    power x 0 = 1
    power x n = x * power x (n - 1)

  As given, 'power' is NOT tail-recursive. Write a tail-recursive version of
  'power', which accumulates its result in a 3rd parameter, 'acc'.
-}

powerTailRecursive :: Int -> Int -> Int
powerTailRecursive x n =
  let
    aux :: Int -> Int -> Int -> Int
    aux x n acc = if (n == 0) then acc
                  else aux x (n - 1) (x * acc)
  in
    aux x (assert (n >= 0) n) 1

{-|
  Q10:

  We can also compute the power of a number as follows:
    power x 0 = 1
    power x (2 * n) = power (x ^ 2) n
    power x (2 * n + 1) = x * power (x ^ 2) n

  Implement such a logarithmtic-time power 
  function tail-recursively.
  
  Hint: In designing your algorithm try to use
         the relationship below
		 
       aux x n acc = (x^n)*acc

-}

powerLogTime :: Int -> Int -> Int
powerLogTime x n =
  let
    aux :: Int -> Int -> Int -> Int
    aux x n acc = if (n == 0) then acc
                  else case ((n `mod` 2) == 0) of 
                    True -> aux (x * x) (n `div` 2) acc
                    False -> aux (x * x) (n `div` 2) (x * acc)
  in
    aux x (assert (n >= 0) n) 1
