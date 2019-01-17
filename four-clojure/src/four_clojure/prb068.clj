(ns four-clojure.prb068)

;; Week 1 - Day 3

;; Recurring Theme
;; Difficulty: Elementary

;; Lesson URL: https://www.4clojure.com/problem/68

;; Clojure only has one non-stack-consuming looping construct: recur.
;; Either a function or a loop can be used as the recursion point. Either way,
;; recur rebinds the bindings of the recursion point to the values it is passed.
;; Recur must be called from the tail-position, and calling it elsewhere will
;; result in an error.
;; NOTE:
;; A tail-position is the place in an expression that would return a value.
;; There are no more forms evaluated after the tail-position.

(def __
  [7 6 5 4 3]
  )

(= __
   (loop [x 5
          result []]
     (if (> x 0)
       (recur (dec x) (conj result (+ 2 x)))
       result)))
;; => true

;; [7 6 5 4 3] - (+ 2 x)
;;  5 4 3 2 1  - x
