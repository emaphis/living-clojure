(ns four-clojure.prb027)

;; Week 1 - Day 4

;; Palindrome Detector
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/27

;; Write a function which returns true if the given sequence is a palindrome.
;; Hint: "racecar"
;;  does not equal '(\r \a \c \e \c \a \r)

#(= (seq %) 
    (reverse %))

(def __
  (fn [x]
    (= (seq x)
       (reverse x)))
  )

(false? (__ '(1 2 3 4 5)))
;; => true
(true? (__ "racecar"))
;; => true
(true? (__ [:foo :bar :foo]))
;; => true
(true? (__ '(1 1 3 3 1 1)))
;; => true
(false? (__ '(:a :b :c)))
;; => true
