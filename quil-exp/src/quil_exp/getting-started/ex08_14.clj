(ns quil-exp.getting-started.ex08_14
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Circular.
;; Example 08-14: Circular Motion
;; pg. 115.

;; Constants
(def OFFSET 60)
(def SCALAR 30)
(def SPEED  0.05)

(defn setup []
  {:angle 0.0})

(defn update-state [state]
  (let [angle  (+ (:angle state) SPEED)]
    {:angle angle}))

(defn draw [state]
  (let [x  (+ OFFSET (* (q/cos (:angle state)) SCALAR))
        y  (+ OFFSET (* (q/sin (:angle state)) SCALAR))]
    (q/ellipse x y 40 40)))

(q/defsketch example8_14
  :title "Circular Motion"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
