(ns four-clojure.prb)

;; Week1 - Day 1

;; http://www.4clojure.com/problem/9

;; Sets: conj

;; Difficulty:	Elementary

;; When operating on a set, the conj function returns a new set with one or
;; more keys "added".

(def a9 2)

(= #{1 2 3 4} (conj #{1 4 3} a9))
;; => true
