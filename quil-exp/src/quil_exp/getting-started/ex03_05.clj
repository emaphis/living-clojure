(ns quil-exp.getting-started.ex03-05
  (:require [quil.core :as q]))

;; Example 3-5: Draw a Rectangle
;; pg. 17.

(defn draw []
  (q/rect 180 60 220 40))

(q/defsketch example3_5
  :size [480 120]
  :draw draw)