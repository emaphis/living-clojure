(ns quil-exp.getting-started.ex03-15
  (:require [quil.core :as q]))

;; Color
;; Example 3-15: Paint with Grays.
;; pg. 25.

(defn draw []
  (q/background 0)             ; Black
  (q/fill 204)                 ; light gray
  (q/ellipse 132 82 200 200)   ; light gray circle
  (q/fill 153)                 ; medium gray
  (q/ellipse 228 -16 200 200)  ; medium gray circle
  (q/fill 102)                 ; dark gray
  (q/ellipse 268 118 200 200)) ; dark gray circle


(q/defsketch example3_15
  :size [480 120]
  :draw draw)
