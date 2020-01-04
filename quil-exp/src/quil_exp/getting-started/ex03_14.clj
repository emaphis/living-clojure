(ns quil-exp.getting-started.ex03-14
  (:require [quil.core :as q]))

;; Drawing Modes
;; Example 3-14: On the Corner
;; pg. 23.

(defn draw []
  (q/rect 120 60 80 80)
  (q/ellipse 120 60 80 80)
  (q/ellipse-mode :corner)
  (q/rect 280 20 80 80)
  (q/ellipse 280 20 80 80))


(q/defsketch example3_14
  :title "On the Corner"
  :size [480 120]
  :draw draw)
