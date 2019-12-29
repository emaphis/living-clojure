(ns quil-exp.getting-started.ex05-04
  (:require [quil.core :as q]))

;;; Follow
;; Example 5-4: Track the Mouse. 
;; pg. 51.


(defn setup []
  (q/fill 0 102)
  (q/no-stroke))

(defn draw []
  (q/ellipse (q/mouse-x) (q/mouse-y) 9 9))


(q/defsketch example5_4
  :size [480 120]
  :setup setup
  :draw draw)
