(ns quil-exp.getting-started.ex04-09
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-9: Kinking the Lines
;; pg. 42.

;; for (int i = 20; i < 400; i += 20) {
;;   line(i, 0, i + i/2, 80);
;;   line(i + i/2, 80, i*1.2 120);
;; }

(defn draw []
  (q/stroke-weight 2)
  (loop [i 20]
    (when (< i 400)
      (q/line i 0 (+ i (/ i 2)) 80)
      (q/line (+ i (/ i 2.0)) 80 (* i 1.2) 120)
      (recur (+ i 20)))))

(defn draw-2 []
  (q/stroke-weight 2)
  (doseq [i (range 20 400 20)]
    (q/line i 0 (+ i (/ i 2)) 80)
    (q/line (+ i (/ i 2.0)) 80 (* i 1.2) 120)))

(q/defsketch example4_9
  :size [480 120]
  :draw draw-2)
