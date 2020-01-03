(ns quil-exp.getting-started.ex08-03
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Speed and Direction
;; Example 8-02: Move a Shape.
;; pg. 105.

(def RADIUS 40)
(def SPEED 0.5)

;; number -> state
(defn make-state [x]
  {:x x})

;; none -> state
(defn setup []
  (q/ellipse-mode :radius)
  (make-state (* -1 RADIUS)))

;; state -> state
(defn update-state [state]
  (let [x (+ (:x state) SPEED)]
    (make-state x)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [x (:x state)]
    (q/arc x 60 RADIUS RADIUS 0.52 5.76)))


(q/defsketch example8_01
  :title "Move a Shape"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
