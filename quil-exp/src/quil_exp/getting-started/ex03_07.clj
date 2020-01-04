(ns quil-exp.getting-started.ex03-07
  (:require [quil.core :as q]))

;; Basic Shapes
;; Example 3-7: Draw Part of an Ellipse.
;; pg. 18.

(defn draw []
  (q/arc  90 60 80 80 0 q/HALF-PI)
  (q/arc 190 60 80 80 0 (+ q/PI q/HALF-PI))
  (q/arc 290 60 80 80 q/PI (+ q/TWO-PI q/HALF-PI))
  (q/arc 390 60 80 80 q/QUARTER-PI (+ q/PI q/QUARTER-PI)))

(q/defsketch example3_7
  :title "Part of an Ellipse"
  :size [480 120]
  :draw draw)
