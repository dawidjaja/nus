module Lab02 where
import           Control.Monad (join)
import qualified Data.List     as L
import           Prelude       hiding (product, reverse)
import Debug.Trace (trace)
{-|

   Please complete `Lab02.hs` and submit it to LumiNUS
   by 11pm, 6th October 2020 (Monday).
   
   Write your name here (individual assignments)
   
      Author: ...
   
   You may add auxiliary methods but
   must not changed the type nor the name of methods that are
   already given in the code below. You can of course complete
   the missing method bodies. Do design some of your own test
   cases.

-}
{-|
   Q1:
   Consider the following implementation of reverse using 'foldr'. It contains a
   bug that causes it to return the wrong result. Can you fix it so that you get
   the following correct outcome?
   > reverse [1, 2, 3]
   [3, 2, 1]
   > reverse [1, 2]
   [2, 1]
   > reverse []
   []
-}
-- reverse :: [a] -> [a]
reverse xs = foldr (\x acc -> acc ++ x) xs []
{-|
   Q2: List Operations
   Using list comprehension syntax, write code for the following 
   operations. 
-}
{-|
   (a)
   Compute the cross product of 2 lists that returns an element from each list as a pair (a, b) e.g.
   > product [1, 2, 3] ['a', 'b']
   [(1, 'a'), (1, 'b'), (2, 'a'), (2, 'b'), (3, 'a'), (3, 'b')]
-}
product :: [a] -> [b] -> [(a, b)]
product xs ys = error "'product' - To be implemented"
{-|
   (b)
   Compute the cross product of 2 lists that returns an integer from each list as
   a pair (a, b), such that a*b > a+b.
   > product2 [1, 2, 3] [2, 7]
   [(3,2),(2,7),(3,7)]
-}
product2 :: [Int] -> [Int] -> [(Int, Int)]
product2 xs ys = error "'product2' - To be implemented"
{-|
   (c)
   Compute the divisor from the product of 2 lists, but only if the second
   element is non-zero.
   > divisorProd [5, 9, 4] [2, 0, 3]
   = [5/2,9/2,4/2,5/3,9/3,4/3]
   = [2, 4, 2, 1, 3, 1]
-}
divisorProd :: [Int] -> [Int] -> [Int]
divisorProd = error "'divisorProd' - To be implemented"
{-|
   Q3:
   Write a function that could count the number of positive, negative, and zero
   elements in a list of numbers;
   (i)  using only 'foldl'
   (ii) using only 'filter' and 'length'
-}
countNums1 :: [Int] -> (Int, Int, Int)
countNums1 = error "'countNums1' - To be implemented with 'foldl'"
countNums2 :: [Int] -> (Int, Int, Int)
countNums2 = error "'countNums2' - To be implemented with 'filter' and 'length'"
{-|
   Q4: Higher-Order functions for trees
   We can implement 2 common higher-order functions, mapTree and foldTree, on
   simple binary trees, as shown below. Use these two higher order functions to
   complete the subsequent questions on binary trees.
-}
data Tree a
   = Leaf a
   | Node a (Tree a) (Tree a)
   deriving Show
t1 :: Tree Int
t1 = Node 3 (Leaf 1) (Leaf 2)
t2 :: Tree Int
t2 = Node 4 t1 (Leaf 6)
t3 :: Tree Int
t3 = Node 5 t2 (Leaf 3)
instance Functor Tree where
   fmap f t =
     case t of
       Leaf v ->
         Leaf (f v)
       Node v leftTree rightTree ->
         Node (f v) (fmap f leftTree) (fmap f rightTree)
mapTree :: (a -> b) -> Tree a -> Tree b
mapTree = fmap
foldTree :: (a -> b) -> (a -> b -> b -> b) -> Tree a -> b
foldTree fLeaf _ (Leaf v) = fLeaf v
foldTree fLeaf fNode (Node v leftTree rightTree) =
   fNode v leftRes rightRes
   where
     leftRes = foldTree fLeaf fNode leftTree
     rightRes = foldTree fLeaf fNode rightTree
{-|
   (a)
   Write a function that would multiply n to every element of a tree.
   > t1
   Node 3 (Leaf 1) (Leaf 2)
   > multN 3 t1
   Node 9 (Leaf 3) (Leaf 6)
-}
multN ::Int -> Tree Int -> Tree Int
multN = error "'addN' - To be implemented"
{-|
   (b)
   Write a function that would return the left most element of a tree
   > t2
   Node 4 (Node 3 (Leaf 1) (Leaf 2)) (Leaf 6)
   > leftMost t2
   1
   > t3
   Node 5 (Node 4 (Node 3 (Leaf 1) (Leaf 2)) (Leaf 6)) (Leaf 3)
   > leftMost t3
   1
-}
leftMost :: Tree a -> a
leftMost = error "'rightMost' - To be implemented"
{-|
   (c)
   Write a function that would mirror a tree around its root element, i.e., a
   tree with its left and right subtrees recursively flipped.
   > t2
   Node 4 (Node 3 (Leaf 1) (Leaf 2)) (Leaf 6)
   > mirrorTree t2
   Node 4 (Leaf 6) (Node 3 (Leaf 2) (Leaf 1))
-}
mirrorTree :: Tree a -> Tree a
mirrorTree = error "'mirrorTree' - To be implemented"
t4 :: Tree Char
t4 = Node 'a' (Leaf 'b') (Node 'c' (Leaf 'e') (Leaf 'f'))
{-|
   (d)
   Write a function that would tag each element of a tree with the size of its
   subtree.
   > t4
   Node 'a' (Leaf 'b') (Node 'c' (Leaf 'e') (Leaf 'f'))
   > addSize t4
   Node (5, 'a') (Leaf (1, 'b')) (Node (3, 'c') (Leaf (1, 'e')) (Leaf (1, 'f')))
-}
addSize :: Tree a -> Tree (Int, a)
addSize = error "'addSize' - To be implemented"
{-|
   (e)
   Write a function to check if a tree of integers is a max-heap, i.e.,
   a tree where each root node of every sub-tree is always the largest element 
   of its entire sub-tree.
   > t1
   Node 3 (Leaf 1) (Leaf 2)
   > checkMaxHeap t1
   True
   > t5
   Node 2 (Leaf 1) (Leaf 3)
   > checkMaxHeap t5
   False
   Often this max-heap is a complete tree, in which every level, except possibly the last, is completely filled, and all nodes are as far left as possible. 
   
   Discuss the complexity of your implementation, and suggest
   suitable programming techniques that may improve its overall efficiency.
  
-}
t5 :: Tree Int
t5 = Node 2 (Leaf 1) (Leaf 3)
checkMaxHeap :: Tree Int -> Bool
checkMaxHeap = error "'checkHeap' - To be implemented"


{-|
   Q5: Pretty printers
   Consider the binary tree defined earlier.
   You have been given a higher-order printer, 'showTree', that returns a tree as
   a string (prints a tree) in prefix form. For example, the tree 't2' would be:
   Node 4
   Node 3
   Leaf 1
   Leaf 2
   Node 3
   Leaf 1
   Leaf 2
   (i) The above pretty printing is not very readable; provide a neater pretty
        printer in 'showTree2' that would provide space indentation to represent
        the depth of each subtree.
        > showTree2 t2
        Node 4
          Node 3
            Leaf 1
            Leaf 2
          Node 3
            Leaf 1
            Leaf 2
   (ii) We can also print a tree in infix form. Complete the 'showTreeInfix'
        function to allow binary trees to be printed in infix order.
         > showTreeInfix t2
             Leaf 1
           Node 3
             Leaf 2
         Node 4
             Leaf 1
           Node 3
             Leaf 2
-}
showTree :: Show a => Tree a -> String
showTree (Leaf v) = "Leaf " ++ show v ++ "\n"
showTree (Node v leftTree rightTree) =
   "Node " ++ show v ++ "\n" ++
   showTree leftTree ++
   showTree rightTree
showTree2 :: Show a => Tree a -> String
showTree2 = error "'showTree2' - To be implemented - neat tree with nested indentation"
showTreeInfix :: Show a => Tree a -> String
showTreeInfix = error "'showTreeInfix' - To be implemented - neat tree with nested indentation in infix form"
{-|
   Q6: Numbered Lists
   We have our own printer for lists in 'showList', which prints a list as a
   comma separated string of its elements, surrounded by square brackets
   > showList ls
   "[\"This\", \"is\", \"a\", \"numbered\", \"list\"]"
   Give a list printer that would number each element of a given list and print
   it.
   > showListNum ", " ls
   "[(1)\"This\", (2)\"is\", (3)\"a\", (4)\"numbered\", (5)\"list\"]"
   You may make use of the addNum function below which adds a number to each
   element of a list.
-}
showList :: Show a => [a] -> String
showList xs = "[" ++ L.intercalate ", " (map show xs) ++ "]"
addNum :: [a] -> [(Int, a)]
addNum xs =
   let
     aux [] _       = []
     aux (y : ys) n = (n, y) : aux ys (n + 1)
   in
     aux xs 1
ls :: [String]
ls = ["This", "is", "a", "numbered", "list"]
showListNum :: Show a => String -> [a] -> String
showListNum separator = error "'showListNum' - To be implemented"
