(ns quil-exp.getting-started.ex03-04
  (:require [quil.core :as q]))

;; Basic Shapes
;; Example 3-4: Draw Basic Shapes
;; pg. 16.

(defn draw []
  (q/quad 158 55 199 14 392 66 351 107)
  (q/triangle 347 54 392 9 392 66)
  (q/triangle 158 55 290 91 290 112))

(q/defsketch example3_4
  :title "Draw Basic Shapes"
  :size [480 120]
  :draw draw)
