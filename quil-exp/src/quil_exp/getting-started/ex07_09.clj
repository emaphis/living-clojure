(ns quil-exp.getting-started.ex07-09
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Shapes
;; Example 7-9: Draw with Shapes.
;; pg. 98.

;; image number -> state
(defn make-state [shape]
  {:shape shape})

;; none -> state
(defn setup []
  (let [network (q/load-shape "resources//network.svg")]
    (make-state network)))

;; state -> state
#_(defn update-state [state]
    state)

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [network (:shape state)]
    (q/shape network 30 10)
    (q/shape network 180 10 280 280)))

(q/defsketch example7_9
  :title "Draw with Shapes."
  :size [480 120]
  :setup setup
  ;;:update update-state
  :draw draw
  :middleware [m/fun-mode])
