(ns quil-exp.getting-started.ex07-02
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Images
;; Example 7-2: Load more Images
;; pg. 91.

;; image image -> state
(defn make-state [img-1 img-2]
  {:img-1 img-1  :img-2 img-2})

;; none -> state
(defn setup []
  (let [img-1 (q/load-image "resources//lunar.jpg")
        img-2 (q/load-image "resources//capsule.jpg")]
    (make-state img-1 img-2)))

;; state -> IO
(defn draw [state]
  (let [img-1 (:img-1 state)
        img-2 (:img-2 state)]
    (q/image img-1 -120 0)
    (q/image img-1 130 0 240 120)
    (q/image img-2 300 0 240 120)))

(q/defsketch example7_2
  :title "Load more Images"
  :size [480 120]
  :setup setup
  :draw draw
  :middleware [m/fun-mode])
