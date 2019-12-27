(ns quil-exp.getting-started.ex04-10
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-10: Embed One for Loop in Another.
;; pg. 44.

;; for (int y = 0; i <= height; i += 40) {
;;   for (int x = 0, x <= width; x += 40) {
;;     fill(255 140);
;;     ellipse(x, y, 40, 40);
;;   }
;; }

(defn draw []
  (q/background 0)
  (q/no-stroke)
  (loop [y 0]
    (when (<= y (q/height))
      (loop [x 0]
        (when (<= x (q/width))
          (q/fill 255 140)
          (q/ellipse x y 40 40)
          (recur (+ x 40))))
      (recur (+ y 40)))))
;; Blech!


(defn draw2 []
  (q/background 0)
  (q/no-stroke)
  (doseq [y (range 0 (inc (q/height)) 40)
          x (range 0 (inc (q/width)) 40)]
    (q/fill 255 140)
    (q/ellipse x y 40 40)))


(q/defsketch example4_10
  :size [480 120]
  :draw draw2)
