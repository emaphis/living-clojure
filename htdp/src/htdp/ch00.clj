(ns htdp.ch00
  (:require [htdp.util :refer :all]))


(defn plop [n]
  (if (< n 6)
    (println "plop: " n)
    (println n ...)))

(defn run [n]
  (if (< n 10)
    (do (plop n)
        (run (inc n)))
    (println "Stop !")))

(run -4)

(doc do)
