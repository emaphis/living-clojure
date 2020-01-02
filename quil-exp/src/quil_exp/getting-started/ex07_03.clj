(ns quil-exp.getting-started.ex07-03
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Images
;; Example 7-3: Mousing Around with Images
;; pg. 92.

;; image number number -> state
(defn make-state [img mouse-x mouse-y]
  {:img img  :mouse-x mouse-x :mouse-y mouse-y})

;; none -> state
(defn setup []
  (let [img (q/load-image "resources//lunar.jpg")]
    (make-state img 0 0)))

;; state -> state
(defn update-state [state]
  (let [img     (:img state )
        mouse-x (q/mouse-x)
        mouse-y (q/mouse-y)]
    (make-state img mouse-x mouse-y)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [img      (:img state)
        mouse-x  (:mouse-x state)
        mouse-y  (:mouse-y state)]
    (q/image img 0 0 (* mouse-x 2) (* mouse-y 2))))

(q/defsketch example7_3
  :title "Mousing Around with Images"
  :size [480 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
