(ns quil-exp.getting-started.ex09_04
  (:require [quil.core :as q]))

;; Function Basics
;; Example 09-04: Two's Company
;; pg. 124.

(defn draw []
  (q/background 176 204 226)

  ;; left owl
  (q/translate 110 110)
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
  (q/quad 0 -58 4 -51 0 -44 -4 -51)

  ;;  right owl
  (q/translate 70 0)
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
  (q/quad 0 -58 4 -51 0 -44 -4 -51))  ; Beak


(q/defsketch example9_04
  :title "Two's Company"
  :size [480 120]
  :draw draw)
