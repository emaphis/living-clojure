(ns quil-exp.getting-started.ex04-13
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-13: Halftone Dots
;; pg. 46.

;; for (int y = 32; i <= height; i += 8) {
;;   for (int x = 12, x <= width; x += 15) {
;;     ellipse(x+y, y, 16 - y/10.0, 16 - y/10.0);
;;   }
;; }

(defn draw []
  (q/background 0)
  (q/fill 255)
  (q/stroke 102)
  (doseq [y (range 32 (inc (q/height)) 8)
          x (range 12 (inc (q/width)) 15)]
    (q/ellipse (+ x y) y (- 16 (/ y 10.0)) (- 16 (/ y 10.0)))))


(q/defsketch example4_13
  :size [480 120]
  :draw draw)
