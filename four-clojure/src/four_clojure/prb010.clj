(ns four-clojure.prb010)

;; http://www.4clojure.com/problem/10

;; Week 1 - Day 1

;; Intro to Maps
;; Difficulty:	Elementary


;; Maps store key-value pairs. Both maps and keywords can be used as lookup
;; functions. Commas can be used to make maps more readable, but they
;; are not required.

(def a10 20)

(= a10 ((hash-map :a 10, :b 20, :c 30) :b))
;; => true
(= a10 (:b {:a 10, :b 20, :c 30}))
;; => true
