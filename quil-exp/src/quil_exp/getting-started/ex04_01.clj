(ns quil-exp.getting-started.ex04-01
  (:require [quil.core :as q]))

;; First Variables.
;; Example 4-1: Reuse the Same Values
;; pg. 35.


(defn draw []
  (let [y 60
        d 80]
    (q/ellipse 75 y d d)
    (q/ellipse 175 y d d)
    (q/ellipse 275 y d d)))


(q/defsketch example4_1
  :size [480 120]
  :draw draw)
