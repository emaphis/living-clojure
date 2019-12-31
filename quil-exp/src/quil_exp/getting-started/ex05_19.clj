(ns quil-exp.getting-started.ex05-19
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Type
;; Example 5-19: Move with Arrow Keys
;; pg. 70.

;; a state is an empty map of a key which is a string

;; string -> state
(defn make-state [x]
  {:x x})

;; IO -> state
(defn setup []
  ;;(q/background 204)
  (make-state 215))

;; state -> state
(defn update-state[state]
  (if (q/key-pressed?)
    (let [key (q/key-as-keyword)
          x   (:x state)]
      (cond
        (= key :right) (make-state (inc x))
        (= key :left)  (make-state (dec x))
        :else state))
    state))

;; state -> IO
(defn draw [state]
  (q/rect (:x state) 45 50 50))

(q/defsketch example5_19
  :title "Move with Arrow Keys"
  :size [480 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
