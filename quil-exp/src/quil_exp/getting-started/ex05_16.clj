(ns quil-exp.getting-started.ex05-16
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Type
;; Example 5-16: Tap a Key
;; pg. 67.

;; a state is an empty map

;; IO -> state
(defn make-state []
  {})

;; IO -> state
(defn setup []
  (make-state))

;; state -> state
(defn update-state [state]
  state)

;; state -> IO
(defn draw [state]
  (q/background 204)
  (q/line 20 20 220 100)
  (if (q/key-pressed?)
    (q/line 220 20 20 100)))


(q/defsketch example5_16
  :title "Tap a Key"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
