(ns quil-exp.getting-started.exMM-MM
  (:require [quil.core :as q]))

;; Main Topic
;; Example MM-MM: Sub Topic 
;; pg. mmm.

(defn setup []
  ())

(defn draw []
  (q/background 200)
  (q/fill 0)
  (let [x 60]
    (q/text (str "x: " x) 10 20)
    (q/ellipse x x x x)) )

(q/defsketch exampleN_NN
  :title "MM"
  :size [240 120]
  :setup setup
  :draw draw)
