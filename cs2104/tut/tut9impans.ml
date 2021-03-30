(* 
   Tutorial 9 : Imperative Programming with OCaml
   Week of 27th Oct 2020 

  Q1 : Consider a tail-recursive method.
  Re-implement this method using an iterative loop together
  with two imperative ref type values that are internal to the method.
  You can try the online interpreter below.
  https://repl.it/languages/ocaml
*)

let fib n =
  let rec aux n a b =
    if n<=0 then (a,b)
    else aux (n-1) (a+b) a
 in fst(aux n 1 0);;


let fib_imp n =
  let a = ref 1 in
  let b = ref 0 in
  for i = n downto 1 do
    let aplusb = !a + !b in
    let () = b := !a in
    let () = a := aplusb in
    ()
  done; !a ;;

let fib_imp2 n =
  let a = ref 1 in
  let b = ref 0 in
  let rec aux n = 
    if n<=0 then ()
    else 
      let aplusb = !a + !b in
      let () = b := !a in
      let () = a := aplusb in
      aux (n-1) in
  let () = aux n in
  !a;;



(* 
   Q2 : The for-loop is implemented as follows.
   What is the polymoprhic type of this method?

  Ans: for_loop:: ini -> int -> (int -> unit) -> unit
*)

let for_loop init final stmt =
  let rec aux i =
    if i<=final then (stmt i; aux (i+1))
    else ()
  in aux init


(* 
   Q3 :  Write two higher-order methods that would
   implement a for-down-to loop iterator, and a while loop method *)

(* let while_loop t1 body = *)
(*   failwith "TBI" *)
(* ;; *)


let rec while_loop (t1:unit -> bool) (body:unit -> unit) : unit =
  if t1 () then (body(); while_loop t1 body)
  else ()
;;


(* let for_loop init final stmt = *)
(*   failwith "TBI" *)
(* ;; *)

let for_loop_down init final stmt =
  let rec aux i =
    if i>=final then (stmt i; aux (i-1))
    else ()
  in aux init


(* 
   Q4 : Implement fib function with memoization 
   using a hash table. 

   How does this compares with your implementation in Q1?

   Ans : memoization is a more geenral memoization (which applies
   to other functions, but require some extra codes to
   maintain table and its retrieval. Memoization also
   leads to extra space cosrs.
*)

(* let fib8 n = *)
(*   let h = Hashtbl.create 10 in *)
(*   let rec aux n = *)
(*     if n<=1 then 1 *)
(*     else  *)
(*       aux (n-1) + aux (n-2) *)
(*   in aux n;; *)

let fib8 =
  let h = Hashtbl.create 10 in
  fun n ->
  let rec aux n =
    if n<=1 then 1
    else 
      try
       Hashtbl.find h n 
      with _ ->
       let r = aux (n-1) + aux (n-2) in
       let () = print_endline ("hash add "^(string_of_int n)) in
       let () = Hashtbl.add h n r in
       r
  in aux n;;
