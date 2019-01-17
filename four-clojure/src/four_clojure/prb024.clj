(ns four-clojure.prb024)

;; Week 1 - Day 4

;; Sum It All Up.
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/24

;;Write a function which returns the sum of a sequence of numbers.

(def __
  (fn [x]
    (reduce + 0 x))
  )

(= (__ [1 2 3]) 6)
;; => true
(= (__ (list 0 -2 5 5)) 8)
;; => true
(= (__ #{4 2 1}) 7)
;; => true
(= (__ '(0 0 -1)) -1)
;; => true
(= (__ '(1 10 3)) 14)
;; => true
