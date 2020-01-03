(ns quil-exp.getting-started.ex09_01
  (:require [quil.core :as q]))

;; Function Basics
;; Example 09-01: Roll the Dice
;; pg. 122.


(defn roll-dice [num-sides]
  (let [d   (+ 1 (int (q/random num-sides)))]
    (println "Rolling..." d)))

(defn setup []
  (println "Ready to roll!")
  (roll-dice 20)
  (roll-dice 20)
  (roll-dice 6)
  (println "Finished."))


(q/defsketch example9_01
  :title "Roll the Dice"
  :size [120 120]
  :setup setup)
