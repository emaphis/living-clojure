(ns quil-exp.getting-started.ex08_15
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Cirxcular.
;; Example 08-15: Spirals
;; pg. 117.

;; Constants
(def OFFSET 60)
(def SPEED  0.05)

(defn setup []
  {:angle 0.0 :scalar 2})

(defn update-state [state]
  (let [angle  (+ (:angle state) SPEED)
        scalar (+ (:scalar state) SPEED)]
    {:angle angle :scalar scalar}))

(defn draw [state]
  (let [x  (+ OFFSET (* (q/cos (:angle state)) (:scalar state)))
        y  (+ OFFSET (* (q/sin (:angle state)) (:scalar state)))]
    (q/ellipse x y 2 2)))

(q/defsketch example8_15
  :title "Spirals"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
