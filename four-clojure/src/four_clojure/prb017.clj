(ns four-clojure.prb017)

;; Week 1 - Day 2

;; Sequences: Maps
;; Difficulty:	Elementary

;; Lesson URL: https://www.4clojure.com/problem/17

;; The `map` function takes two arguments: a function (f) and a `sequence` (s).
;; Map returns a new `sequence` consisting of the result of applying f to
;; each item of s.
;; Do not confuse the `map` function with the `map` data structure.

(def v17 (list 6 7 8))

(= v17 (map #(+ % 5) '(1 2 3))) ;; => true
