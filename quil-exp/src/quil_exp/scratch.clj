(ns quil-exp.scratch
  (:require [quil.core :as q]))

(defn draw []
  (if (q/mouse-pressed?)
    (q/fill 0)
    (q/fill 255))
  (let [x (q/mouse-x)
        y (q/mouse-y)
        width 80
        height 80]
    (q/ellipse x y width height)))

(defn setup []
  (q/background 255))

(q/defsketch scratch
  :size [480 120]
  :draw draw
  :setup setup)
