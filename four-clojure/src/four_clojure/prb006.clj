(ns four-clojure.prb006)

;; http://www.4clojure.com/problem/6

;; Intro to Vectors

;; Vectors can be constructed several ways. You can compare them with lists. 

(def a6 [:a :b :c])

(= a6 (list :a :b :c) (vec '(:a :b :c)) (vector :a :b :c))
;; => true
