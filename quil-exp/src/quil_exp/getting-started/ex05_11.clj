(ns quil-exp.getting-started.ex05-11
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Click
;; Example 5-11: Detect when Not Clickers.
;; pg. 58.

;; state is map with an integer :stroke
(defn make-state [stroke] {:stroke stroke})

;; IO -> state
(defn setup []
  (q/stroke-weight 30)
  (make-state 0))

;; state -> state
(defn update-state [state]
  (if (q/mouse-pressed?)
    (make-state 0)
    (make-state 255)))

;; state -> IO
(defn draw [state]
  (q/background 204)  ; erase object
  (q/stroke 102)
  (q/line 40 0 70 (q/height))
  (q/stroke (:stroke state))
  (q/line 0, 70 (q/width) 50))


(q/defsketch example5_11
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
