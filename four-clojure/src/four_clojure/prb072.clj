(ns four-clojure.prb072)

;; Week 1 - Day 3

;; Rearranging Code: `->>`
;; Difficulty: Elementary

;; Lesson URL: https://www.4clojure.com/problem/72

;; This problem uses the thread-last macro, `->>`, which is much like the
;; thread-first macro. `->>` is much like the thread-first macro. The major
;; difference is that it threads the expression as the last argument through the
;; forms. This is especially useful if you want to use threading on collection
;; functions like `map`, `filter`, and `take` where the collection is the last
;; argument:

(->> [1 2 3 4 5 6 7 8] (filter even?) (take 3))
;; => (2 4 6)

;; The thread-last macro threads an expression `x` through a variable number of
;; forms. First, `x` is inserted as the last item in the first form, making a
;; list of it if it is not a list already.
;; Then the first form is inserted as the last item in the second form, making a
;; list of that form if necessary. This process continues for all the forms.
;; Using thread-last can sometimes make your code more readable.

(= (reduce + (map inc (take 3 (drop 2 [2 5 4 1 3 6]))))
   (->> [2 5 4 1 3 6] (drop 2) (take 3) (map inc) (reduce +))
   11)
