(ns quil-exp.getting-started.ex06-10
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Push and Pop
;; Example 6-20: Isolating Transformations
;; pg. 84.

;; a state is a map of a x a mouse coordinate to translate 
;; nnumber number -> state
(defn make-state [x y]
  {:x x :y y})

;; IO -> state
(defn setup []
  (make-state 0 0))

;; state -> state
(defn update-state[state]
  (let [x (q/mouse-x)
        y (q/mouse-y)]
    (make-state x y)))

;; state -> IO
(defn draw [state]
  (let [x (:x state)
        y (:y state)]
    (q/push-matrix)
    (q/translate x y)
    (q/rect 0 0 30 30)
    (q/pop-matrix)
    (q/translate 35 10)
    (q/rect 0 0 15 15)))


(q/defsketch example6_10
  :title "Isolating Transformations"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
