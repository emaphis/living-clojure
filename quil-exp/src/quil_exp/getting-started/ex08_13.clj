(ns quil-exp.getting-started.ex08_13
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Circular.
;; Example 08-13: Sine Wave Movement
;; pg. 115.

;; Constants
(def OFFSET 60)
(def SCALAR 40)
(def SPEED  0.05)

(defn setup []
  {:angle 0.0})

(defn update-state [state]
  (let [angle  (+ (:angle state) SPEED)]
    {:angle angle}))

(defn draw [state]
  (q/background 0)
  (let [y1  (+ OFFSET (* (q/sin (:angle state)) SCALAR))
        y2  (+ OFFSET (* (q/sin (+ (:angle state) 0.4)) SCALAR))
        y3  (+ OFFSET (* (q/sin (+ (:angle state) 0.8)) SCALAR))]
    (q/ellipse  80 y1 40 40)
    (q/ellipse 120 y2 40 40)
    (q/ellipse 160 y3 40 40)))

(q/defsketch example8_13
  :title "Sine Wave Movement"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
