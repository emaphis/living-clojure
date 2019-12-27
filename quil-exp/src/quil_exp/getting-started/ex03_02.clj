(ns quil-exp.getting-started.ex03-02
  (:require [quil.core :as q]))

;; The Display Window
;; Example 3-2: Drawing a Point
;; pg. 14.

(defn draw []
  (q/point 240 60))

(q/defsketch example3_2
  :size [480 120]
  :draw draw)
