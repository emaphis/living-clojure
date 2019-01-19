(ns four-clojure.prb041)

;; Week 1 - Day 5

;; Drop Every Nth Item
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/41

;; Write a function which drops every Nth item from a sequence.

(
 (fn drop-n [lst n m]
   (if (empty? lst)
     ()
     (conj (drop-n (next lst) (dec n)) (first lst))))

 [1 2 3 4] 4 0)


(= (__ [1 2 3 4 5 6 7 8] 3) [1 2 4 5 7 8])
(= (__ [:a :b :c :d :e :f] 2) [:a :c :e])
(= (__ [1 2 3 4 5 6] 4) [1 2 3 5 6])
