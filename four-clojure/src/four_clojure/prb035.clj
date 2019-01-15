(ns four-clojure.prb035)

;; Week 1 - Day 2

;; Local bindings
;; Difficulty:	Elementary

;;Lesson URL: https://www.4clojure.com/problem/35

;; Clojure lets you give local names to values using the special let-form.

(def __ 7)

(= __ (let [x 5] (+ 2 x))) ;; => true
(= __ (let [x 3, y 10] (- y x))) ;; => true
(= __ (let [x 21] (let [y 3] (/ x y)))) ;; => true
