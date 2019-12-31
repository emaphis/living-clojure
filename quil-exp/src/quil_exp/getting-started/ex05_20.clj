(ns quil-exp.getting-started.ex05-21
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Map
;; Example 5-21: Map with the (map) function.
;; pg. 71.

;; none -> state
(defn make-state []
  { })

;; IO -> state
(defn setup []
  (q/stroke-weight 12)
  (make-state))

;; state -> state
(defn update-state[state]
  (make-state))

;; state -> IO
(defn draw [state]
  (q/background 204)
  (q/stroke 102)
  (let [x  (q/mouse-x)
        mx (q/map-range x 0 (q/width) 60 180)]
    (q/line x 0 x (q/height))      ; Gray line
    (q/stroke 0)
    (q/line mx 0 mx (q/height))))  ; Black line

(q/defsketch example5_21
  :title "Map with the (map) function"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
