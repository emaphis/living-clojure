(ns quil-exp.getting-started.ex05-01
  (:require [quil.core :as q]))

;; Once and Forever.
;; Example 5-1: The (draw []) Function.
;; pg. 49.


(defn draw []
  ;; Displays the frame count to th Console
  (println "I'm drawing")
  (println (q/frame-count)))


(q/defsketch example5_1
  :draw draw)
