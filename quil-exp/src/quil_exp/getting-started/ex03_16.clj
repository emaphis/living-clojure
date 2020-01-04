(ns quil-exp.getting-started.ex03-16
  (:require [quil.core :as q]))

;; Color
;; Example 3-16: Control Fill and Stroke
;; pg. 25.

(defn draw []
  (q/fill 153)                 ; medium gray
  (q/ellipse 132 82 200 200)   ; gray circle
  (q/no-fill)                  ; turn off fill
  (q/ellipse 228 -16 200 200)  ; outline cirlce
  (q/no-stroke)                ; turn off storke
  (q/ellipse 268 118 200 200)) ; doesn't draw!


(q/defsketch example3_16
  :title "Control Fill and Stroke"
  :size [480 120]
  :draw draw)
