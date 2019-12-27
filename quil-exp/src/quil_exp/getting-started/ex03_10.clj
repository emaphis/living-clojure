(ns quil-exp.getting-started.ex03-10
  (:require [quil.core :as q]))

;; Drawing Order
;; Example 3-10: Put It in Reverse.
;; pg. 20.

(defn draw []
  (q/rect 160 30 260 20)
  ;; The ellipse draws on top of the rectangle
  ;; because it comes after in the code.
  (q/ellipse 140 0 190 190))

(q/defsketch example3_10
  :size [480 120]
  :draw draw)
