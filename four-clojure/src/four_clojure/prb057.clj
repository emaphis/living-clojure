(ns four-clojure.prb057)

;; Week 1 - Day 3

;; Simple Recursion
;; Difficulty: Elementary

;; Lesson URL: https://www.4clojure.com/problem/57
;; A recursive function is a function which calls itself. This is one of the
;; fundamental techniques used in functional programming.

(def __
  '(5 4 3 2 1)
  )

(= __ ((fn foo [x] (when (> x 0) (conj (foo (dec x)) x))) 5))
;; => true
