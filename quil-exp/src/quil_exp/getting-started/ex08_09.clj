(ns quil-exp.getting-started.ex08-09
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Random
;; Example 8-09: Draw Randomly
;; pg. 110.

;; Constants
(def SPEED 2.5)
(def DIAMETER 20)

;; float float -> state
(defn make-state [x y]
  {:x x  :y y})

;; none -> state
(defn setup []
  (make-state (/ (q/width) 2.0)
              (/ (q/height) 2.0)))  ; center of the screen

;; state -> state
(defn update-state [state]
  (let [x  (+ (:x state) (q/random (* -1 SPEED) SPEED))
        y  (+ (:y state) (q/random (* -1 SPEED) SPEED))
        x  (q/constrain x 0 (q/width))
        y  (q/constrain y 0 (q/height))]
    (make-state x y)))


;; state -> IO
(defn draw [state]
  (let [x  (:x state)
        y  (:y state)]
    (q/ellipse x y DIAMETER DIAMETER)))

(q/defsketch example8_09
  :size [240 120]
  :title "Draw Randomly"
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
