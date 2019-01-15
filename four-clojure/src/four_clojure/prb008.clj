(ns four-clojure.prb008)

;; http://www.4clojure.com/problem/8

;;; Intro to Sets
;; Difficulty:	Elementary

;; Sets are collections of unique values.

(def a8 #{:a :b :c :d})

(= a8 (set '(:a :a :b :c :c :c :c :d :d)))
;; => true
(= a8 (clojure.set/union #{:a :b :c} #{:b :c :d}))
;; => true
