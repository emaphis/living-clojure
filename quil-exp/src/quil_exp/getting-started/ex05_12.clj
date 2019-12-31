(ns quil-exp.getting-started.ex05-12
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Click
;; Example 5-12: Multiple Mouse Buttons.
;; pg. 60.

;; state is map with an integer :stroke
(defn make-state [stroke] {:stroke stroke})

;; IO -> state
(defn setup []
  (q/stroke-weight 30)
  (make-state 102))

;; state -> state
(defn update-state [state]
  (if (q/mouse-pressed?)
    (if (= (q/mouse-button) :left)
      (make-state 255)
      (make-state 0))
    (make-state 204)))

;; state -> IO
(defn draw [state]
  (q/background 204)  ; erase object
  (q/stroke 102)
  (q/line 40 0 70 (q/height))
  (q/stroke (:stroke state))
  (q/line 0, 70 (q/width) 50))


(q/defsketch example5_12
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
