(ns quil-exp.getting-started.ex07-05
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Images
;; Example 7-5:  Transparency with a PNG
;; pg. 94.

;; image number -> state
(defn make-state [img-1 mouse-y]
  {:img-1 img-1  :mouse-y mouse-y})

;; none -> state
(defn setup []
  (let [img-1 (q/load-image "resources//clouds.png")]
    (make-state img-1 0)))

;; state -> state
(defn update-state [state]
  (let [img-1   (:img-1 state)
        mouse-y (q/mouse-y)]
    (make-state img-1 mouse-y)))

;; state -> IO
(defn draw [state]
  (q/background 204)
  (let [img-1   (:img-1 state)
        mouse-y (:mouse-y state)]
    (q/image img-1 0 0)
    (q/image img-1 0 (* -1 mouse-y))))

(q/defsketch example7_5
  :title "Transparency with a PNG"
  :size [480 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
