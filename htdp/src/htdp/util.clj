(ns htdp.util
  (:require [clojure.repl :refer :all]))


(defn ...
  ""
  ([] ~(println "error: must be implemented"))
  ([a]  ~(println "error: must be implemented"))
  ([a b]  ~(println "error: must be implemented"))
  ([a b c]  ~(println "error: must be implemented"))
  ([a b c d]  ~(println "error: must be implemented"))
  ([a b c d e]  ~(println "error: must be implemented")))

(comment

  (defn either [n]
    (if (> n 5)
      (... ... map ...)
      (... 5)))

  (either 4)
  )


(defn string=? [s1 s2]
  (if (= (compare s1 s2) 0) true false))

(defn string>? [s1 s2]
  (if (< (compare s1 s2) 0) true false))

(defn string<? [s1 s2]
  (if (> (compare s1 s2) 0) true false))

(comment
  (string<? "a" "m")
  (string<? "m" "m")
  (string<? "z" "m")

  (compare "a" "m") ;; => -2
  (compare "m" "m") ;; => 0
  (compare "z" "m") ;; => 13
  )

;; tail recursion


(defn what [coll]
  (loop [item (first coll)
         coll coll]
    (if (empty? (rest coll))
      item
      (recur (first (rest coll)) (rest coll)))))

(defn what-2 [coll]
  (loop [[item & remaining] coll]
    (if (seq remaining)
      item
      (recur remaining))))

(what-2 [1 2 3 4])
(what-2 '(1 2 3 4))
(what-2 #{1 2 3 4})
