(ns four-clojure.prb011)

;; Week 1 - Day 1

;; http://www.4clojure.com/problem/11

;; Maps: conj

;; Difficulty:	Elementary

;; When operating on a `map`, the `conj` function returns a new `map` with one
;; or more key-value pairs "added".

(def a11 [:b 2])

(= {:a 1, :b 2, :c 3} (conj {:a 1} a11  [:c 3]))
;; => true
