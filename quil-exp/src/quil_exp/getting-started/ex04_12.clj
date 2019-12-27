(ns quil-exp.getting-started.ex04-12
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-12: Pins and Lines
;; pg. 46.

;; for (int y = 20; i <= height-20; i += 10) {
;;   for (int x = 20, x <= width-20; x += 10) {
;;     ellipse(x, y, 4, 4);
;;     line(x,y 240, 60);
;;   }
;; }

(defn draw []
  (q/background 0)
  (q/fill 255)
  (q/stroke 102)
  (doseq [y (range 20 (- (q/height) 19) 10)
          x (range 20 (- (q/width) 19) 10)]
    (q/ellipse x y 4 4)
    ;; Draw a line to the center of the display.
    (q/line x y, 240 60)))


(q/defsketch example4_10
  :size [480 120]
  :draw draw)
