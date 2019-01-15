(ns four-clojure.prb012)

;; Week 1 - Day 1

;; http://www.4clojure.com/problem/12

;; Intro to Sequences

;; Difficulty:	Elementary

;; All Clojure collections support sequencing. You can operate on sequences
;; with functions like first, second, and last.

(= 3 (first '(3 2 1)))
;; => true
(= 3 (second [2 3 4]))
;; => true
(=  (last (list 1 2 3)))
;; => true
