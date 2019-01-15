(ns four-clojure.prb003)

;; http://www.4clojure.com/problem/3

;; Intro to Strings

;; Difficulty:	Elementary

;; Clojure strings are Java strings. This means that you can use any of the Java
;; string methods on Clojure strings.

(def p3 "HELLO WORLD")

(= p3 (.toUpperCase "hello world")) ;; => true
