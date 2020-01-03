(ns quil-exp.getting-started.ex08_11
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Timers.
;; Example 08-11: Triggering Timed Events
;; pg. 112.

;; Constants
(def TIME-1 2000)
(def TIME-2 4000)

(defn setup []
  {:x 0.0})

(defn update-state [state]
  (let [current-time  (q/millis)
        x' (:x state)
        x  (cond (> current-time TIME-2) (- x' 0.5)
                 (> current-time TIME-1) (+ x' 2.0)
                 :else x')]
    {:x x}))

(defn draw [state]
  (q/background 204)
  (let [x (:x state)]
    (q/ellipse x 60 90 90)))

(q/defsketch example8_11
  :title "Triggering Timed Events"
  :size [480 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
