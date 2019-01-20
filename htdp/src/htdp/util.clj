(ns htdp.util
  (:require [clojure.repl :refer :all]))


(defmacro ...
  ""
  ([] `(println "error: must be implemented"))
  ([a]  `(println "error: must be implemented"))
  ([a b]  `(println "error: must be implemented"))
  ([a b c]  `(println "error: must be implemented"))
  ([a b c d]  `(println "error: must be implemented"))
  ([a b c d e]  `(println "error: must be implemented")))

(defn either [n]
  (if (> n 5)
    (... ... map ...)
    (... 5)))

(either 4)
