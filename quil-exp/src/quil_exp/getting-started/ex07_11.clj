(ns quil-exp.getting-started.ex07-11
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; TODO:  Quil doesn't seem to have create-shape functionality.

;; Shapes
;; Example 7-11: Creating a New Shape.
;; pg. 100.

;; image number number -> state
(defn make-state [shape mouse-x]
  {:shape shape :mouse-x mouse-x})

;; none -> state
(defn setup []
  (make-state "dino" 0))

;; state -> state
(defn update-state [state]
  (let [shape   (:shape state)
        mouse-x (q/mouse-x)]
    (make-state shape mouse-x)))

;; state -> IO
(defn draw [state]
  (q/background 204)
  (let [dino     (:shape state)
        mouse-x (:mouse-x state)]
    (q/translate (- mouse-x 120) 0)
    (q/begin-shape)
    (q/fill 153 176 180)
    (q/vertex 50 120)
    (q/vertex 100 90)
    (q/vertex 110 60)
    (q/vertex 80 20)
    (q/vertex 210 60)
    (q/vertex 160 80)
    (q/vertex 200 90)
    (q/vertex 140 100)
    (q/vertex 130 120)
    (q/end-shape :close))
  ;;(q/shape dino 0 0)
  )

(q/defsketch example7_11
  :title "Creating a New Shape"
  :size [480 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
