(ns four-clojure.prb020)

;; Week 1 - Day 4

;; Penultimate Element
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/20

;; Write a function which returns the second to last element from a sequence.

(fn [lst] (second (reverse lst)))

(def __
  (fn [lst]
    (-> lst
        (reverse)
        (second)))
  )

(= (__ (list 1 2 3 4 5)) 4)
;; => true
(= (__ ["a" "b" "c"]) "b")
;; => true
(= (__ [[1 2] [3 4]]) [1 2])
;; => true
