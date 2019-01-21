(ns htdp.ch02.sec01-functions
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]
            [clojure.string :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Functions

;; useless function definition
(defn f [x] 1)

(defn g [x y] (+ 1 1))

(defn h [x y z] (+ (* 2 2) 3))

(h 1 2 3) ;; => 7

;; a useful function
(defn ff [a]
  (* 10 a))

;; applications:
(f 1)
;; => 1

(f "hello world")
;; => 1

(f true)
;; => 1

(ff 3)
;; => 30

(+ (ff 3) 2)
;; => 32

(* (ff 4) (+ (ff 3) 2))
;; => 1280

(ff (ff 1))
;; => 100


;; Ex 11  Define a function that consumes two numbers, x and y, and that
;; computes the distance of point (x,y) to the origin.

;; In exercise 1 you developed the right-hand side of this function for
;; concrete values of x and y. Now add a header.

(defn distance [x y]
  (Math/sqrt (+ (* x x) (* y y))))

(distance 12 5)
;; => 13.0
(distance 3 4)
;; => 5.0


;; Ex 12. Define the function `cvolume`, which accepts the length of a side
;; of an equilateral cube and computes its volume.
;; If you have time, consider defining csurface, too.

(defn cvolume [s]
  (* s s s))

(defn csurface [s]
  (* 8 s s))


;; Ex 13.  Define the function string-first, which extracts the first
;; 1String from a non-empty string.

(defn string-first [st]
  (subs st 0 1))

(string-first "L") ;; => "L"
(string-first "Hello") ;; => "H"


;; Ex 14. Define the function string-last, which extracts the last 1String
;; from a non-empty string.

(defn string-last [st]
  (subs st (dec (count st))))

(string-last "L") ;; => "L"
(string-last "Hello") ;; => "o"


;; Ex 15. Define `==>`. The function consumes two Boolean values, call them
;; sunny and friday. Its answer is #true if sunny is false or friday is true.
;; Note Logicians call this Boolean operation implication, and they use the
;; notation sunny => friday for this purpose.

(defn ==> [sunny friday]
  (or (not sunny) friday))

(==> false false) ;; => true
(==> false true)  ;; => true
(==> true false)  ;; => false
(==> true true)   ;; => true


;; Ex 18. Define the function `string-join`, which consumes two strings and
;; appends them with "_" in between. See exercise 2 for ideas.

(defn string-join [st1 st2]
  (str st1 "_" st2))

(string-join "hello" "world")
;; => "hello_world"


;; Ex 19. Define the function `string-insert`, which consumes a string `str`
;; plus a number `i` and inserts "_" at the ith position of `str`. Assume `i`
;; is a number between 0 and the length of the given string (inclusive).
;; See exercise 3 for ideas. Ponder how string-insert copes with "".

;; (str (subs str1 0 i) "_" (subs str1 i))

(defn string-insert [st i]
  (str (subs st 0 i) "_" (subs st i)))

(string-insert "helloworld" 5)
;; => "hello_world"

(string-insert "" 0) ;; => "_"
(string-insert "ab" 1);; => "a_b"


;; Ex 20.  Define the function `string-delete`, which consumes a string plus a
;; number `i` and deletes the ith position from `str`. Assume `i` is a number
;; between 0 (inclusive) and the length of the given string (exclusive).
;; See exercise 4 for ideas. Can string-delete deal with empty strings?

;; (str (subs str1 0 i) (subs str1 (inc i) (count str1)))

(defn string-delete [st i]
(str (subs st 0 i) (subs st (inc i) (count st))))

(string-delete "helloworld" 5)
;; => "helloorld"

;; (string-delete "" 0) ;; nope. fails
(string-delete "a" 0);; => ""
(string-delete "ab" 0);; => "b"
(string-delete "ab" 1);; => "a"
