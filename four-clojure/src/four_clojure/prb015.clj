(ns four-clojure.prb015)

;; Week 1 - Day 2

;; Intro to Functions
;; Difficulty:	Elementary

;; Lesson URL: https://www.4clojure.com/problem/15
;; Write a function which doubles a number.

#_(def __
    (fn [num] (* 2 num))
    )
(def __
  #(* 2 %)
  )

(= (__ 2) 4) ;; => true
(= (__ 3) 6) ;; => true
(= (__ 11) 22) ;; => true
(= (__ 7) 14) ;; => true
