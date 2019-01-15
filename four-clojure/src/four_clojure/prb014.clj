(ns four-clojure.prb014)

;; Week 1 - Day 2

;; http://www.4clojure.com/problem/14

;; Intro to Functions
;; Difficulty:	Elementary

;; Clojure has many different ways to create functions.

(def __  8)

(= __ ((fn add-five [x] (+ x 5)) 3)) ;; => true
(= __ ((fn [x] (+ x 5)) 3)) ;; => true
(= __ (#(+ % 5) 3)) ;; => true
(= __ ((partial + 5) 3)) ;; => true
