(ns quil-exp.for-the-glory-of-art
  (:require [quil.core :as q]))

;; Example from:
;; https://github.com/quil/quil

(defn setup []
  (q/frame-rate 1)
  (q/background 200))

(defn draw []
  (q/stroke (q/random 255))
  (q/stroke-weight (q/random 10))
  (q/fill (q/random 255))

  (let [diam (q/random 100)
        x    (q/random (q/width))
        y    (q/random (q/height))]
    (q/ellipse x y diam diam)))

(q/defsketch example
  :title "Oh so many grey circles"
  :settings #(q/smooth 2)
  :setup setup
  :draw draw
  :size [323 200])
