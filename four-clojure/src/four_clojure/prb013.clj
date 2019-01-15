(ns four-clojure.prb013)

;; Week 1 - Day 2
;; http://www.4clojure.com/problem/13

;; Sequences: rest

;; Difficulty: Elementary


;; The rest function will return all the items of a sequence except the first.

(def a13 [20 30 40])

(= a13 (rest [10 20 30 40]))
;; => true
