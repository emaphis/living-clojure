(ns quil-exp.getting-started.ex06-09
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Scale
;; Example 6-09: Keeping strokes Consistent
;; pg. 83.

;; a state is a map of a x a mouse coordinate to translate 
;; number number number -> state
(defn make-state [x y scalar]
  {:x x :y y :scalar scalar})

;; IO -> state
(defn setup []
  (make-state 0 0 0.0))

;; state -> state
(defn update-state[state]
  (let [x      (q/mouse-x)
        y      (q/mouse-y)
        scalar (/ x 60.0)]
    (make-state x y scalar)))

;; state -> IO
(defn draw [state]
  (let [x       (:x state)
        y      (:y state)
        scalar (:scalar state)]
    (q/translate x y)
    (q/scale scalar)
    (q/stroke-weight (/ 1.0 scalar))
    (q/rect -15 -15 30 30)))

(q/defsketch example6_08
  :title "Scaling"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
