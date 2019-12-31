(ns quil-exp.getting-started.ex05-20
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Map
;; Example 5-20: Map Values to a Range
;; pg. 71.

;; string -> state
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
        mx (+ (/ x 2) 60)]
    (q/line x 0 x (q/height))      ; Gray line
    (q/stroke 0)
    (q/line mx 0 mx (q/height))))  ; Black line

(q/defsketch example5_20
  :title "Map Values to a Range"
  :size [480 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
