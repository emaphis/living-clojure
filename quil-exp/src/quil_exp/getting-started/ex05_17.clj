(ns quil-exp.getting-started.ex05-17
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Type
;; Example 5-17: Draw Some Letters
;; pg. 68.

;; a state is an empty map of a key which is a string

;; IO -> state
(defn make-state [key]
  {:key key})

;; IO -> state
(defn setup []
  (q/text-size 64)
  (q/text-align :center)
  (make-state "_"))

(defn update-state[state]
  (make-state (str (q/raw-key))))

(defn draw [state]
  (q/background 0)
  (q/fill 255)
  (q/text (:key state) 60 80))

(q/defsketch example5_17
  :title "Draw Some Letters"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
