(ns quil-exp.getting-started.ex05-03
  (:require [quil.core :as q]))

;; Once and Forever.
;; Example 5-3: Global Variale
;; pg. 51.

(def x 280)
(def y -100)
(def diameter 380)

(defn setup []
  (q/fill 102))

(defn draw []
  (q/background 204)
  (q/ellipse x y diameter diameter))


(q/defsketch example5_3
  :size [480 120]
  :setup setup
  :draw draw)
