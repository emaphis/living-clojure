(ns htdp.ch01.sec01-numbers
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Arithmetic of Numbers
;;  Atomic values of numbers, strings, booleans

;; primitive expression
(+ 1 2)
;; => 3

(+ 1 (+ 1 (+ 1 1) 2) 3 4 5)
;; => 18

;;; Primitives
;; + - * / abs inc ceiling denominator expt floor gcd log max numerator
;; quottient random remainder sqr tan.

(Math/sin 0)
;; => 0.0

(Math/E)
;; => 2.718281828459045

(/ 4 6)
;; => 2/3

;; sqrt is inexact in Clojure/Java
(Math/sqrt 2)
;; => 1.4142135623730951


;; Ex 1: Add the following definitions for x and y

(def x 12)
(def y 5)

;; Now imagine that x and y are the coordinates of a Cartesian point.
;; Write down an expression that computes the distance of this point to the
;; origin, that is, a point with the coordinates (0,0).

(Math/sqrt (+ (* x x) (* y y)))
;; => 13.0
