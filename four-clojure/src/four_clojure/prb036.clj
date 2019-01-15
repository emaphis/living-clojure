(ns four-clojure.prb036)

;; Week 1 - Day 2

;; Let it Be
;; Difficulty:	Elementary

;; Lesson URL: https://www.4clojure.com/problem/36

;;Can you bind x, y, and z so that these are all true?

(= 10 (let [x 7, y 3, z 1] (+ x y))) ;; => true
(= 4 (let  [x 7, y 3, z 1] (+ y z)))  ;; => true
(= 1 (let  [x 7, y 3, z 1] z)) ;; => true
