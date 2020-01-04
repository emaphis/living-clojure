(ns quil-exp.getting-started.ex03-13
  (:require [quil.core :as q]))

;; Shape Properties
;; Example 3-13: Set Stroke Joins
;; pg. 22.

(defn draw []
  (q/stroke-weight 12)
  (q/rect  60 25 70 70)
  (q/stroke-join :round)  ; Round th stroke corners
  (q/rect 160 25 70 70)
  (q/stroke-join :bevel) ; Bevel the stroke corners
  (q/rect 260 25 70 70)
  (q/stroke-join :miter)   ; Miter the stroke corners
  (q/rect 360 25 70 70))


(q/defsketch example3_13
  :title "Set Stroke Joins"
  :size [480 120]
  :draw draw)
