(ns four-clojure.prb007)

;; http://www.4clojure.com/problem/7

;; Vectors: conj
;; Difficulty:	Elementary

;; When operating on a Vector, the conj function will return a new vector with
;; one or more items "added" to the end.

(def a7 [1 2 3 4])

(= a7 (conj [1 2 3] 4))
;; => true
(= a7 (conj [1 2] 3 4))
;; => true
