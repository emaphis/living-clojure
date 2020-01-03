(ns quil-exp.getting-started.ex09_05
  (:require [quil.core :as q]))

;; Function Basics
;; Example 09-05: An Owl Function
;; pg. 127.

(defn owl [x y]
  (q/push-matrix)
  (q/translate x y)
  (q/stroke 138 138 125)
  (q/stroke-weight 70)
  (q/line 0 -35 0 -65)   ; Body
  (q/no-stroke)
  (q/fill 255)
  (q/ellipse -17.5 -65 35 36)  ; Left eye Dome
  (q/ellipse 17.5 -65 35 35)   ; Right eye Dome
  (q/arc 0 -65 70 70 0 q/PI)   ; Chin
  (q/fill 51 51 30)
  (q/ellipse -14 -65 8 8)  ; Left eye
  (q/ellipse 14 -65 8 8)   ; Right eye
  (q/quad 0 -58 4 -51 0 -44 -4 -51)  ; Beak
  (q/pop-matrix))

(defn draw []
  (q/background 176 204 226)
  (owl 110 110)
  (owl 180 110))

(q/defsketch example9_05
  :title "An Owl Function"
  :size [480 120]
  :draw draw)
