(ns quil-exp.getting-started.ex03-02
  (:require [quil.core :as q]))

;; Example 3-2: Drawing a Point
;; pg. 14.

(defn draw []
  (q/point 240 60))

(q/defsketch example3_2
  :size [480 120]
  :draw draw)

;; primitives
line(x1, y1, x2, y2)
triangle(x1, y1, x2, y2, x3, y3);
quad(x1, y2, x2, y2, x3, y3, x4, y4);
rect (x, y, width, height);
ellipse(x, y, width, height);
arc(x, y, width, height, start, stop);
