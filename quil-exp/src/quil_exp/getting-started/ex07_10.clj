(ns quil-exp.getting-started.ex07-10
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Shapes
;; Example 7-10: Scaling Shapes.
;; pg. 99.

;; image number number -> state
(defn make-state [shape mouse-x width]
  {:shape shape :mouse-x mouse-x :width width})

;; none -> state
(defn setup []
  (let [network (q/load-shape "resources//network.svg")]
    (q/shape-mode :center)
    (make-state network 0 0)))

;; state -> state
(defn update-state [state]
  (let [shape   (:shape state)
        mouse-x (q/mouse-x)
        width   (q/width)]
    (make-state shape mouse-x width)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [network (:shape state)
        mouse-x (:mouse-x state)
        width   (:width state)
        diameter (q/map-range mouse-x 0 width 10 800)]
    (q/shape network 120 60 diameter diameter)))

(q/defsketch example7_10
  :title "Scaling Shapes"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
