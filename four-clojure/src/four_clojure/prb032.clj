(ns four-clojure.prb032)

;; Week 1 - Day 4

;; Duplicate a Sequence
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/32
;; Write a function which duplicates each element of a sequence.

;; TODO:  find a better solution

(defn dup [x]
  (if (empty? x)
    ()
    (conj
     (conj (dup (rest x))
           (first x))
     (first x))))

(dup [1 2 3])
(dup '(1 2 3))

(def __
  (fn dup [x]
    (if (empty? x)
      ()
      (conj
       (conj (dup (rest x))
             (first x))
       (first x))))  
  )

(= (dup [1 2 3]) '(1 1 2 2 3 3))
(= (dup [:a :a :b :b]) '(:a :a :a :a :b :b :b :b))
(= (dup [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4]))
(= (dup [[1 2] [3 4]]) '([1 2] [1 2] [3 4] [3 4]))
