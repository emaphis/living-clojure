(ns quil-exp.getting-started.ex05-06
  (:require [quil.core :as q]))

;;;Follow
;; Example 5-6: Draw Continuously
;; pg. 53.


(defn setup []
  (q/stroke-weight 4)
  (q/stroke 0 102))

(defn draw []
  (q/line (q/mouse-x) (q/mouse-y) (q/pmouse-x) (q/pmouse-y)))


(q/defsketch example5_6
  :size [480 120]
  :setup setup
  :draw draw)
