(ns quil-exp.getting-started.ex04-02
  (:require [quil.core :as q]))

;; First Variables.
;; Example 4-2: Change Values
;; pg. 36.


(defn draw []
  (let [y 100
        d 130]
    (q/ellipse 75 y d d)
    (q/ellipse 175 y d d)
    (q/ellipse 275 y d d)))


(q/defsketch example4_2
  :size [480 120]
  :draw draw)
