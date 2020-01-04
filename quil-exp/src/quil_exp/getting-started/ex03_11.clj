(ns quil-exp.getting-started.ex03-11
  (:require [quil.core :as q]))

;; Shape Properties
;; Example 3-11: Set Stroke Weight
;; pg. 21.

(defn draw []
  (q/stroke-weight 1)
  (q/ellipse 75 60 90 90)
  (q/stroke-weight 8) ; Stroke weight to 8 pixels
  (q/ellipse 175 60 90 90)
  (q/ellipse 279 60 90 90)
  (q/stroke-weight 20) ; Stroke weight to 20 pixels
  (q/ellipse 389 60 90 90))

(q/defsketch example3_11
  :title "Set Stroke Weight"
  :size [480 120]
  :draw draw)
