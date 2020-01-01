(ns quil-exp.getting-started.ex06-07
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Rotate
;; Example 6-07: An Articulating Arm
;; pg. 81.

;; a state is a map of a x a mouse coordinate to rotate and angle
;; number number -> state
(defn make-state [angle angle-direction]
  {:angle angle :angle-direction angle-direction})

;; IO -> state
(defn setup []
  (make-state 0.0 1))

(def SPEED 0.005)

;; state -> state
(defn update-state[state]
  (let [angle  (+ (:angle state) (* SPEED (:angle-direction state) ))
        angle-direction  (if (or  (> angle q/QUARTER-PI)
                                  (< angle 0))
                           (- 0.0 (:angle-direction state))
                           (:angle-direction state))]
    (make-state angle angle-direction)))

;; state -> IO
(defn draw [state]
  (let [angle (:angle state)]
    (q/background 204)
    (q/translate 20 25)  ; Move to start position
    (q/rotate angle)
    (q/stroke-weight 12)
    (q/line 0 0 40 0)
    (q/translate 40 0)   ; Move to next joint
    (q/rotate (* angle 2.0))
    (q/stroke-weight 6)
    (q/line 0 0 30 0)
    (q/translate 30 0)   ; Move to next joint
    (q/rotate (* angle  2.5))
    (q/stroke-weight 3)
    (q/line 0 0 20 0)))

(q/defsketch example6_07
  :title "An Articulating Arm"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
