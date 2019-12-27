(ns quil-exp.getting-started.ex03-06
  (:require [quil.core :as q]))

;; Example 3-6: Draw an Ellipse
;; pg. 17.

(defn draw []
  (q/ellipse 278 -100 400 400)
  (q/ellipse 120  100 110 100)
  (q/ellipse 412   60  18  18))

(q/defsketch example3_6
  :size [480 120]
  :draw draw)
