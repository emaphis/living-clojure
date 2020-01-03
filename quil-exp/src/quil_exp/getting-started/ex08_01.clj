(ns quil-exp.getting-started.ex08-01
  (:require [quil.core :as q]))

;; Frames
;; Example 8-01: See the Frame Rate 
;; pg. 103.

(defn draw []
  (println (q/current-frame-rate)))


(q/defsketch example8_01
  :title "See the Frame Rate"
  :draw draw)
