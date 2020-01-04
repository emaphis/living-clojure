(ns quil-exp.getting-started.ex03-17
  (:require [quil.core :as q]))

;; Color
;; Example 3-17: Draw with Color
;; pg. 25.

(defn draw []
  (q/no-stroke)
  (q/background 0 26 51)       ; Dark blue color
  (q/fill 255 0 0)             ; Red color
  (q/ellipse 132 82 200 200)   ; Red Circle
  (q/fill 0 255 0)             ; Green color
  (q/ellipse 228 -16 200 200)  ; Green circle
  (q/fill 0 0 255)             ; Blue color
  (q/ellipse 268 118 200 200)) ; Red circle


(q/defsketch example3_17
  :title "Draw with Color"
  :size [480 120]
  :draw draw)
