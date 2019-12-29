(ns quil-exp.getting-started.ex05-02
  (:require [quil.core :as q]))

;; Once and Forever.
;; Example 5-2: The (setup []) Function
;; pg. 50.

(defn setup []
  (println "I'm starting"))

(defn draw []
  (println "I'm running"))


(q/defsketch example5_2
  :setup setup
  :draw draw)
