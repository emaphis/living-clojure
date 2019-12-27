(ns quil-exp.getting-started.ex03-18
  (:require [quil.core :as q]))

;; Color
;; Example 3-18: Set Transparency
;; pg. 27.

(defn draw []
  (q/no-stroke)
  (q/background 204 226 255)    ; Light blue color
  (q/fill 255 0 0 160)          ; Red color
  (q/ellipse 132 82 200 200)    ; Red Circle
  (q/fill 0 255 0 160)          ; Green color
  (q/ellipse 228 -16 200 200)   ; Green circle
  (q/fill 0 0 255 160)          ; Blue color
  (q/ellipse 268 118 200 200))  ; Red circle


(q/defsketch example3_18
  :size [480 120]
  :draw draw)
