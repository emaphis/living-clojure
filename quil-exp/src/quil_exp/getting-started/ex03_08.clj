(ns quil-exp.getting-started.ex03-08
  (:require [quil.core :as q]))

;; Basic Shapes
;; Example 3-8: Draw with Degrees.
;; pg. 19.

(defn draw []
  (q/arc  90 60 80 80 0 (q/radians 90))
  (q/arc 190 60 80 80 0 (q/radians 270))
  (q/arc 290 60 80 80 (q/radians 180) (q/radians 450))
  (q/arc 390 60 80 80 (q/radians 45) (q/radians 225)))

(q/defsketch example3_8
  :title "Draw with Degrees"
  :size [480 120]
  :draw draw)
