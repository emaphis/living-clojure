(ns quil-exp.getting-started.ex05-05
  (:require [quil.core :as q]))

;;;Follow
;; Example 5-5: The Dot Follows You
;; pg. 52.


(defn setup []
  (q/fill 0 102)
  (q/no-stroke))

(defn draw []
  (q/background 204)
  (q/ellipse (q/mouse-x) (q/mouse-y) 9 9))


(q/defsketch example5_5
  :size [480 120]
  :setup setup
  :draw draw)
