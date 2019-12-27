(ns quil-exp.getting-started.ex03-03
  (:require [quil.core :as q]))

;; Example 3-3: Draw a Line
;; pg. 16.

(defn draw []
  ((q/line 20 50 420 110)))

(q/defsketch example3_3
  :size [480 120]
  :draw draw)