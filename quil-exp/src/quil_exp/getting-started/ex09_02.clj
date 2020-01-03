(ns quil-exp.getting-started.ex09_02
  (:require [quil.core :as q]))

;; Function Basics
;; Example 09-01: Another Way to Roll
;; pg. 123.

(defn setup []
  (println "Ready to roll!")
  (let [d  (+ 1 (int (q/random 20)))]
    (println "Rolling..." d))
  (let [d  (+ 1 (int (q/random 20)))]
    (println "Rolling..." d))
  (let [d  (+ 1 (int (q/random 6)))]
    (println "Rolling..." d))
  (println "Finished."))

(q/defsketch example9_02
  :title "Another Way to Roll"
  :size [120 120]
  :setup setup)
