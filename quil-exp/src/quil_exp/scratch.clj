(ns quil-exp.scratch
  (:require [quil.core :as q]))

(defn setup []
  ())

(defn draw []
  (q/background 170 210 230)
  (q/fill 0)
  (q/text "Hello World" 30 48))

(q/defsketch scratch
  :title "Scratch Pad"
  :size [500 500]
  :setup setup
  :draw draw)
