(ns quil-exp.getting-started.ex08-08
  (:require [quil.core :as q]))

;; Random
;; Example 8-08: Move Shapes Randomly
;; pg. 109.


(defn draw []
  (q/background 204)
  (doseq [x (range 20 (q/width) 20)]
    (let [mx       (/ (q/mouse-x) 10)
          offset-a (q/random (- 1.0 mx) mx)
          offset-b (q/random (- 1.0 mx) mx)]
      (q/line (+ x offset-a) 20 (- x offset-b) 100))))

(q/defsketch example8_08
  :size [240 120]
  :title "Draw Randomly"
  :draw draw)
