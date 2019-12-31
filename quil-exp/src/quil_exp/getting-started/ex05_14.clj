(ns quil-exp.getting-started.ex05-14
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Click
;; Example 5-14: The Bounds of a Cirlce
;; pg. 63.

;; state is map with an integer :stroke
(defn make-state [x y radius fill]
  {:x x :y y :radius radius :fill fill})

;; IO -> state
(defn setup []
  (q/ellipse-mode :radius)
  (make-state 120 60 12 255))

;; state -> state
(defn update-state [state]
  (let [x   (:x state)
        y   (:y state)
        radius (:radius state)
        dist   (q/dist (q/mouse-x) (q/mouse-y) x y)]
    ;;(println dist ":" radius)
    (if (< dist radius) ; mouse is inside the circle.
      (make-state x y (inc radius) 0)
      (make-state x y radius 255))))

;; state -> IO
(defn draw [state]
  (q/background 204)  ; erase object
  (q/fill (:fill state))
  (q/ellipse (:x state) (:y state) (:radius state) (:radius state)))


(q/defsketch example5_14
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
