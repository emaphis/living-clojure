(ns quil-exp.getting-started.exNN-NN
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Main Topic
;; Example NN-NN: Sub Topic 
;; pg. nnn.

;; any  -> state
(defn make-state [x]
  {:x x})

;; none -> state
(defn setup []
  (make-state 0))

;; state -> state
(defn update-state [state]
  (make-state (inc (:x state))))

;; state -> IO
(defn draw [state]
  (let [x (:x state)]
    (q/background 200)
    (q/fill 0)
    (q/text (str "x: " (:x state)) 10 20)
    (q/ellipse x x x x)))

(q/defsketch exampleN_NN
  :title "NN"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
