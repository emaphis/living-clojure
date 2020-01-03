(ns quil-exp.getting-started.ex09_07
  (:require [quil.core :as q]))

;; Function Basics
;; Example 09-07: Owls of Different Sizes
;; pg. 130.

(defn owl [x y g s]
  (q/push-matrix)
  (q/translate x y)
  (q/scale s)
  (q/stroke (- 138 g) (- 138 g) (- 125 g)) ; Set the color value
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
  (q/random-seed 0)
  (doseq [i (range 35 (+ (q/width) 40) 40)]
    (let [gray   (int (q/random 0 102))
          scalar (q/random 0.25 1.0)]
      (owl i 110 gray scalar))))

(q/defsketch example9_07
  :title "Owls of Different Sizes"
  :size [480 120]
  :draw draw)
