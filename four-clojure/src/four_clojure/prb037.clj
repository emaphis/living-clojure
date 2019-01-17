(ns four-clojure.prb037)

;; Week 1 - Day 3

;; Regular Expressions
;; Difficulty:	Elementary

;; Lesson URL: https://www.4clojure.com/problem/37

;; Regular expressions in Clojure are prefixed by a “#”. The re-seq function
;; finds and returns a sequence of matches for a string. For example, if we want
;; to find all the matches for “jam” in a string:

(re-seq #"jam" "I like jam in my jam ")
;; => ("jam" "jam")

;; Regex patterns use reader macro, which is different from a regular macro.
;; The reader maps certain special characters to special behavior.
;; The “#” symbol followed by a doublequoted string tells the reader that it is
;; a regex pattern.
;; We are also using the apply function here, which takes a function and applies
;; it to an argument list:

(apply str [1 2 3])
;; => "123"

;; Now, onto the problem.
;; Regex patterns are supported with a special reader macro.

(def __
  "ABC"
  )

(= __ (apply str (re-seq #"[A-Z]+" "bA1B3Ce ")))
;; => true

;; #"[A-Z]+" matches capital letters A -> Z.
