(ns quil-exp.getting-started.ex05-07
  (:require [quil.core :as q]))

;;;Follow
;; Example 5-7: Set Line Thickness
;; pg. 54.


(defn setup []
  (q/stroke 0 102))

(defn draw []
  (let [weight (q/dist (q/mouse-x) (q/mouse-y) (q/pmouse-x) (q/pmouse-y))]
    (q/stroke-weight weight)
    (q/line (q/mouse-x) (q/mouse-y) (q/pmouse-x) (q/pmouse-y))))


(q/defsketch example5_7
  :size [480 120]
  :setup setup
  :draw draw)
