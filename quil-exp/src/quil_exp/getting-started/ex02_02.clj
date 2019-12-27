(ns quil-exp.getting-started.ex02-02
  (:require [quil.core :as q]))

;; Your First Program
;; Example 2-2: Make Circles.
;; pg. 9.

(defn setup []
  (q/smooth))

(defn draw []
  (if (q/mouse-pressed?)
    (q/fill 0)
    (q/fill 255))
  (q/ellipse (q/mouse-x) (q/mouse-y) 80 80))


(q/defsketch example2_2
  :size [480 120]
  :setup setup
  :draw draw)
