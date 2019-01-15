(ns four-clojure.prb005)

;; http://www.4clojure.com/problem/5

;; Lists: conj
;; Difficulty:	Elementary

;; When operating on a list, the conj function will return a new list with one
;; or more items "added" to the front.

;; Note that there are two test cases, but you are expected to supply only one
;; answer, which will cause all the tests to pass.

(def a5 '(1 2 3 4))

(= a5 (conj '(2 3 4) 1))
;; => true
(= a5 (conj '(3 4) 2 1))
;; => true
