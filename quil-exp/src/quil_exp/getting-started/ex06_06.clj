(ns quil-exp.getting-started.ex06-06
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Rotate
;; Example 6-06: Rotation, Then Translation
;; pg. 80.


;; a state is a map of a x a mouse coordinate to rotate and angle

;; number number number -> state
(defn make-state [x y angle]
  {:x x :y y :angle angle})

;; IO -> state
(defn setup []
  (make-state 0 0 0.0))

;; state -> state
(defn update-state[state]
  (let [x     (q/mouse-x)
        y     (q/mouse-y)
        angle (+ (:angle state) 0.1)]
    (make-state x y angle)))

;; state -> IO
(defn draw [state]
  (q/rotate (:angle state))
  (q/translate (:x state) (:y state))
  (q/rect -15 -15 30 30))

(q/defsketch example6_06
  :title "Rotation, Then Translation"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
