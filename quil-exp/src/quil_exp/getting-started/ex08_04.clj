(ns quil-exp.getting-started.ex08-04
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Speed and Direction
;; Example 8-04: Wrap Around
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
  (let [sv-x (+ (:x state) SPEED)
        x  (if (> sv-x (+ (q/width) RADIUS))
             (* -1 RADIUS)
             sv-x)]
    (make-state x)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [x (:x state)]
    (q/arc x 60 RADIUS RADIUS 0.52 5.76)))


(q/defsketch example8_04
  :title "Wrap Around"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
