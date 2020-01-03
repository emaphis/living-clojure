(ns quil-exp.getting-started.ex08-10
  (:require [quil.core :as q]))

;; Timers
;; Example 8-10: Time Passes
;; pg. 112.

(defn draw []
  (let [timer  (q/millis)]
    (println timer)))

(q/defsketch example8_07
  :title "Time Pases"
  :draw draw)
