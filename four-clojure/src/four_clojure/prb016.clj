(ns four-clojure.prb016)

;; Week 1 - Day 2

;; Hello World
;; Difficulty:	Elementary

;; Lesson URL: https://www.4clojure.com/problem/16
;; Write a function which returns a personalized greeting.

#_(def f16
    (fn [name] (str "Hello, " name "!"))
    )
(def f16
  #(str "Hello, " % "!")
  )

(= (f16 "Dave") "Hello, Dave!") ;; => true
(= (f16 "Jenn") "Hello, Jenn!") ;; => true
(= (f16 "Rhea") "Hello, Rhea!") ;; => true
