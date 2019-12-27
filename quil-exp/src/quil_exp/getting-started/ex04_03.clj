(ns quil-exp.getting-started.ex04-03
  (:require [quil.core :as q]))

;; Processing Variables
;; Example 4-3: Adjust the Size, See What Follows
;; pg. 38.


(defn draw []
  (q/line 0 0 (q/width) (q/height))
  (q/line (q/width) 0 0 (q/height))
  (q/ellipse (/ (q/width) 2) (/ (q/height) 2) 60 60))


(q/defsketch example4_3
  :size [480 120]
  :draw draw)
