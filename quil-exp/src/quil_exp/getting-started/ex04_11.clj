(ns quil-exp.getting-started.ex04-11
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-11: Rows and Columns 
;; pg. 45.

;; for (int y = 0; y <= height+45; y += 40) {
;;    fill(255, 140);
;;    ellipse(0, y, 40, 40);
;; }
;; for (int x = 0; x <= width+45; x += 40) {
;;    fill(255, 140);
;;    ellipse(x, 0, 40, 40);
;; }


(defn draw []
  (q/background 0)
  (q/no-stroke)
  (doseq [y (range 0 (+ (q/height) 45) 40)]
    (q/fill 255 140)
    (q/ellipse 0 y 40 40))
  (doseq [x (range 0 (+ (q/width) 45) 40)]
    (q/fill 255 140)
    (q/ellipse x 0 40 40)))


(q/defsketch example4_11
  :size [480 120]
  :draw draw)
