(ns quil-exp.getting-started.ex06-08
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Scale
;; Example 6-08: Scaling
;; pg. 82.

;; a state is a map of a x a mouse coordinate to translate 
;; number number -> state
(defn make-state [x y]
  {:x x :y y})

;; IO -> state
(defn setup []
  (make-state 0 0))

;; state -> state
(defn update-state[state]
  (let [x      (q/mouse-x)
        y      (q/mouse-y)]
    (make-state x y)))

;; state -> IO
(defn draw [state]
  (let [x      (:x state)
        y      (:y state)
        scalar (/ x 60.0)]
    (q/translate x y)
    (q/scale scalar)
    (q/rect -15 -15 30 30)))

(q/defsketch example6_08
  :title "Scaling"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
