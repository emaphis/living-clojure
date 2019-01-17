(ns four-clojure.prb071)

;; Week 1 - Day 3

;; Rearranging Code: `->`
;; Difficulty: Elementary

;; https://www.4clojure.com/problem/71

;; This problem uses the thread-first macro, `->`. This makes code more readable
;; by threading and expression through the forms. This really helps when you
;; have deeply nested functions or transformations. For example, if you want to
;; take the first element from a list, turn it into a string, and then turn that
;; into uppercase, you could do this:

(.toUpperCase (str (first [:cat :dog :fish])))
;; => ":CAT"

;; Using the thread-first macro, you can rewrite this as:

(-> [:cat :dog :fish] first str .toUpperCase)
;; => ":CAT"


;; Use of this macro makes code more readable and concise.
;; The thread-first macro
;; `->`
;; threads an expression `x` through a variable number of forms. First, `x` is
;; inserted as the second item in the first form, making a list of it if it is
;; not a list already. Then the first form is inserted as the second item in the
;; second form, making a list of that form if necessary.
;; This process continues for all the forms. Using thread-first can sometimes
;; make your code more readable.

(def __
  'last
  )

(= (last (sort (rest (reverse [2 5 4 1 3 6]))))
   (-> [2 5 4 1 3 6]
       (reverse)
       (rest)
       (sort)
       (last))
   5)
;; => true

;; [6 3 1 4 5 2] -> [3 1 4 5 2] -> [1 2 3 4 5]
