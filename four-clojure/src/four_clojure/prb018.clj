(ns four-clojure.prb018)

;; Week 1 - Day 2

;; Sequences: filter
;; Difficulty:	Elementary

;; Lesson URL: https://www.4clojure.com/problem/18

;; The `filter` function takes two arguments: a predicate function (f) and a
;; sequence (s). Filter returns a new sequence consisting of all the items
;; of s for which (f item) returns true.

(def v18 (list 6 7))

(= v18 (filter #(> % 5) '(3 4 5 6 7))) ;; => true
