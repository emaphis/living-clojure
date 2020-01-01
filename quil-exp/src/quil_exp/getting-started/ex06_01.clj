(ns quil-exp.getting-started.ex06-01
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Translate
;; Example 6-01: Translating Location
;; pg. 76.

;; a state is a map of a x and y value to translate.

;; number number -> state
(defn make-state [x y]
  {:x x :y y})

;; IO -> state
(defn setup []
  (make-state 0 0))

;; state -> state
(defn update-state[state]
  (let [x  (q/mouse-x)
        y  (q/mouse-y)]
    (make-state x y)))

;; state -> IO
(defn draw [state]
  (q/translate (:x state) (:y state))
  (q/rect 0 0 30 30))

(q/defsketch example6_1
  :title "Translating Location"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
