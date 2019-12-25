(ns htdp.util
  (:require [clojure.repl :refer :all]))


(defn ---
  "Dummy function for defining templates"
  ([] (println "error: must be implemented"))
  ([a]  (println "error: must be implemented"))
  ([a b]  (println "error: must be implemented"))
  ([a b c]  (println "error: must be implemented"))
  ([a b c d]  (println "error: must be implemented"))
  ([a b c d e]  (println "error: must be implemented")))


(comment

  (defn either [n]
    (if (> n 5)
      (--- --- map ---)
      (--- 5)))

  (either 5)

  (either 4)
  )


(defn string=?
  "Are Strings equal"
  [str1 str2]
  (= (compare str1 str2) 0))

(defn string>?
  "Is `str1` greater than `str2`?"
  [str1 str2]
  (< (compare str1 str2) 0))

(defn string<? [s1 s2]
  "Is `str1` less than `str2`?"
  (> (compare s1 s2) 0))

(comment
  (string=? "aa" "aa")
  (string=? "aa" "bb")
  
  (string>? "a" "m")
  (string>? "m" "m")
  (string>? "z" "m")
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

(what-2 [1 2 3 4]) ;; => 1
(what-2 '(1 2 3 4)) ;; => 1
(what-2 #{1 2 3 4}) ;; => 1
