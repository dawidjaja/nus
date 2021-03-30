{-
    Lab3 - Re-Grading your Quiz 1 with Haskell 

    This lab is to support re-grading your Quiz 1 in Haskell.

    A skeleton code is provided.
    
    Quiz-1-2020.csv contains your captured answers.
    quiz1-workings.txt contains a description of
    the expected answers. In the Quiz, MCQ questions
    from 1-21 and Q24,Q25 are given one mark each, while
    multiple response question Q22-Q23 are given two marks each.

    Regrading is meant to support the following
      1. Allowing Q12,Q16 to also have
         "None of the Given Answers" as correct answer
      2. Each unattempted question to be given 0.2 marks

    After re-grading, the revised marks are shown
    are shown in adjusted_mark_Quiz1.txt.

    You are to revise the Haskell program to obtain
    the following outcomes exactly without unnecessary spaces.

    Output Format:
    ===================
    Score (Sorted by ID):
    <ID>, <Score>
    ...
    <ID>, <Score>

    Score (Sorted by Score):
    <ID>, <Score>
    ...
    <ID>, <Score>

    Min:XX.X,Max:XX.X,Median:XX.X,Average:XX.XX

-}

import           Data.List
-- import Text.Parsec
import           Text.ParserCombinators.Parsec
import           Data.Csv
import           Data.Array

-- type GivenAnswers = [(String, Int, String, [Char])]

{- 
["Student ID","Total Marks","Duration","Start Time","Submit Time","Status","Comment","Q1 Answer","Q1 Mark","Q2 Answer","Q2 Mark","Q3 Answer","Q3 Mark","Q4 Answer","Q4 Mark","Q5 Answer","Q5 Mark","Q6 Answer","Q6 Mark","Q7 Answer","Q7 Mark","Q8 Answer","Q8 Mark","Q9 Answer","Q9 Mark","Q10 Answer","Q10 Mark","Q11 Answer","Q11 Mark","Q12 Answer","Q12 Mark","Q13 Answer","Q13 Mark","Q14 Answer","Q14 Mark","Q15 Answer","Q15 Mark","Q16 Answer","Q16 Mark","Q17 Answer","Q17 Mark","Q18 Answer","Q18 Mark","Q19 Answer","Q19 Mark","Q20 Answer","Q20 Mark","Q21 Answer","Q21 Mark","Q22 Answer","Q22 Answer","Q22 Answer","Q22 Answer","Q22 Answer","Q22 Mark","Q23 Answer","Q23 Answer","Q23 Answer","Q23 Answer","Q23 Mark","Q24 Answer","Q24 Rationale","Q24 Mark","Q25 Answer","Q25 Rationale","Q25 Mark"]
66
-}

{-
   12 - accept "None of the Given Answers"
   15 - accept "None of the Given Answers" (already the correct answer)
   16 - accept "None of the Given Answers"
-}
main = do
  dd <- parseFromFile csvFile "Quiz-1-2020.csv"
  -- putStrLn "parseFile "
  case dd of
    Left _ -> return ()
    Right d ->
      let
        x     = d !! 1
        stud  = drop 2 d
        stud2 = filter (\r -> ((r !! 0) /= "") && ((r !! 1) /= "")) stud
         -- above removes empty entries
        conv m = m
        stud_res = fmap
          (\r ->
            let
              p    = drop 7 r
              mark = array
                (1, 25)
                (fmap
                  (\(i, e) -> (i, conv e))
                  (  [ (i, p !! ((i * 2) - 1)) | i <- [1 .. 21] ]
                  ++ [ (i + 21, p !! ((21 * 2 - 1) + 6)) | i <- [1] ]
                  ++ [ (i + 21, p !! ((21 * 2 - 1) + 6 + 5)) | i <- [2] ]
                  ++ [ (i + 23, p !! ((21 * 2 - 1) + 6 + 5 + (i * 3)))
                     | i <- [1 .. 2]
                     ]
                  )
                )
              ans = array
                (1, 25)
                (  [ (i, p !! ((i * 2) - 1 - 1)) | i <- [1 .. 21] ]
                ++ [ (i + 21, "mult reponse") | i <- [1 .. 2] ]
                ++ [ (i + 23, p !! ((21 * 2 - 1) + 6 + 5 + (i * 3) - 2))
                   | i <- [1 .. 2]
                   ]
                )
            in
              (r !! 0, r !! 1, mark, ans)
          )
          stud2
              -- id, orig_total_mark, orig_mark, stud_answer
      in
        (  putStrLn (show (d !! 0))
        >> putStrLn ("Number of Columns:" ++ (show (length x)))
        >> putStrLn ("Number of Students:" ++ (show (length stud2)))
        >> putStrLn (show (stud_res !! 0))
        )



