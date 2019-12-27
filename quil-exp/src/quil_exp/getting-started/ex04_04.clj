(ns quil-exp.getting-started.ex04-04
  (:require [quil.core :as q]))

;; A Little Math
;; Example 4-4: Basic Arithmetic
;; pg. 39.

;; Remember: Clojure is a functional language.

(defn draw []
  (let [x 25
        h 20
        y 25]
    (q/rect x y 300 h)

    (let [x (+ x 100)]
      (q/rect x (+ y h) 300 h)

      (let [x (- x 250)]
        (q/rect x (+ y (* 2 h)) 300 h)))))

;; Separate calculations from side-effects.
(defn draw-2 []
  (let [x1 25
        h  20
        y1 25
        x2 (+ x1 100)
        y2 (+ y1 h)
        x3 (- x2 250)
        y3 (+ y1 (* 2 h))] ; y2 + h
    (q/rect x1 y1 300 h)
    (q/rect x2 y2 300 h)
    (q/rect x3 y3 300 h)))


(q/defsketch example4_4
  :size [480 120]
  :draw draw-2)
