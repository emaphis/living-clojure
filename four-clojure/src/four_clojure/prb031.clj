(ns four-clojure.prb031)

;; Week 1 - Day 5

;; Pack a Sequence
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/31
;;Write a function which packs consecutive duplicates into sub-lists.

(def __
  (fn pack [coll]
    (if (empty? coll)
      ()
      (let [fst (first coll)
            nxt (next coll)
            acc (pack nxt)]
        (if (= fst (first nxt))
          (conj (next acc) (conj (first acc) fst ))
          (conj acc (conj () fst))))))
  )

(= (__ [1 1 2 1 1 1 3 3]) '((1 1) (2) (1 1 1) (3 3)))
;; => true
(= (__ [:a :a :b :b :c]) '((:a :a) (:b :b) (:c)))
;; => true
(= (__ [[1 2] [1 2] [3 4]]) '(([1 2] [1 2]) ([3 4])))
;; => true
