(ns quil-exp.getting-started.ex05-15
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Click
;; Example 5-15: The Bounds of a rectangle
;; pg. 64.

;; state is map with x y w h fill
(defn make-state [x y width height fill]
  {:x x :y y :width width :height height :fill fill})

;; IO -> state
(defn setup []
  (make-state 80 30 80 60  255))

;; state -> state
(defn update-state [state]
  (let [x      (:x state)
        y      (:y state)
        width  (:width state)
        height (:height state)]
    (if (and  (> (q/mouse-x) x) (< (q/mouse-x) (+ x width))
              (> (q/mouse-y) y) (< (q/mouse-y) (+ y height)))
      (make-state x y width height 0)
      (make-state x y width height 255)) ))

;; state -> IO
(defn draw [state]
  (q/background 204)
  (q/fill (:fill state))
  (q/rect (:x state) (:y state) (:width state) (:height state)))


(q/defsketch example5_15
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
