(ns four-clojure.prb030)

;; Week 1 - Day 5

;; Compress a Sequence
;; Difficulty: Easy

;; Lesson URL: https://www.4clojure.com/problem/30

;; Write a function which removes consecutive duplicates from a sequence.

;; TODO: better solution.

(
 (fn [coll acc]
   (if (empty? coll)
     acc
     (if (= (first coll) (second coll))
       (recur (rest coll) acc)
       (recur (rest coll) (conj acc (first coll)))
       )))
 [1 2 3 4 4 5 6] ())

(

 [ 1 2 3 3 4 5 5 6])

(def __
(fn compress [coll]
(if (empty? coll)
()
(let [fst (first coll)
nxt (next coll)]
(if (= fst (first nxt))
(compress nxt)
(conj (compress nxt) fst)))))
)

(= (apply str (__ "Leeeeeerrroyyy")) "Leroy")
;; => true
(= (__ [1 1 2 3 3 2 2 3]) '(1 2 3 2 3))
;; => true
(= (__ [[1 2] [1 2] [3 4] [1 2]]) '([1 2] [3 4] [1 2]))
;; => true
