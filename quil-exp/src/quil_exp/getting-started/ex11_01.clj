(ns quil-exp.getting-started.ex11-01
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; From Variables to Arrays
;; Example 11-1: Man
;; pg. 150.
;; Update of ex08_03

;; number -> state
(defn make-state [x1 x2]
  {:x1 x1 :x2 x2})

;; none -> state
(defn setup []
  (q/no-stroke)
  (make-state -20 20))

;; state -> state
(defn update-state [state]
  (let [x1 (+ (:x1 state) 0.5)
        x2 (+ (:x2 state) 0.5)]
    (make-state x1 x2)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [x1 (:x1 state)
        x2 (:x2 state)]
    (q/arc x1 30 40 40 0.52 5.76)
    (q/arc x2 90 40 40 0.52 5.76)))

(q/defsketch example11_01
  :title "Many Variables"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
