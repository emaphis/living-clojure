(ns quil-exp.fun-mode-sketch
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Example from:
;; https://github.com/quil/quil/wiki/Functional-mode-%28fun-mode%29


(def min-r 10)

;; IO -> state
(defn setup []
  "Produce initial state"
  {:x 0 :y 0 :r min-r})

;; state -> state
(defn update [state]
  "Increase radius of the circle by 1 on each frame"
  (update-in state [:r] inc))

;; state -> IO
(defn draw [state]
  (q/background 255)  ; erase
  (q/ellipse (:x state) (:y state) (:r state) (:r state)))

;; number -> number
(defn shrink
  "Decrease radius by 1 but keeping it not less `min-r`"
  [radius]
  (max min-r (dec radius)))

(defn mouse-moved [state event]
  (-> state
      ;; set circle position to mouse position
      (assoc :x (:x event) :y (:y event))
      ;; decrease to radius
      (update-in [:r] shrink)))

(q/defsketch example
  :size [300 300]
  :setup setup
  :draw draw
  :update update
  :mouse-moved mouse-moved
  :middleware [m/fun-mode])
