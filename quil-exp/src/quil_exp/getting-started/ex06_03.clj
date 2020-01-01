(ns quil-exp.getting-started.ex06-03
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Rotate
;; Example 6-03: Corner Rotation
;; pg. 78.

;; a state is a map of a x a mouse coordinate to rotate

;; number -> state
(defn make-state [x]
  {:x x})

;; IO -> state
(defn setup []
  (make-state 0))

;; state -> state
(defn update-state[state]
  (let [x  (q/mouse-x)]
    (make-state x)))

;; state -> IO
(defn draw [state]
  (q/rotate (/ (:x state) 100.0))
  (q/rect 40 30 160 20))

(q/defsketch example6_03
  :title "Corner Rotations"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
