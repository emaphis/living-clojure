(ns four-clojure.prb025)

;; Week 1 - Day 4

;; Find the odd numbers
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/25

;; Write a function which returns only the odd numbers from a sequence.

(def __
  (fn [x]
    (filter odd? x))
  )

(= (__ #{1 2 3 4 5}) '(1 3 5))
;; => true
(= (__ [4 2 1 6]) '(1))
;; => true
(= (__ [2 2 4 6]) '())
;; => true
(= (__ [1 1 1 3]) '(1 1 1 3))
;; => true
