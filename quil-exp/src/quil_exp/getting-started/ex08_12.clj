(ns quil-exp.getting-started.ex08_11
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Circlular
;; Example 08-12: Sine Wave Values.
;; pg. 115.


(defn setup []
  {:angle 0.0})

(defn update-state [state]
  (let [angle  (+ (:angle state) 0.1)]
    {:angle angle}))

(defn draw [state]
  (let [sinval  (q/sin (:angle state))
        gray    (q/map-range sinval -1 1 0 255)]
    (println sinval)
    (q/background gray)))

(q/defsketch example8_12
  :title "Sine Wave Values"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
