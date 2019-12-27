(ns quil-exp.getting-started.ex04-08
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-8: Fanning Out the Lines
;; pg. 43.

;; for (int i = 20; i < 400; i += 20) {
;;   line(i, 0, i + i/2, 80);
;; }

(defn draw []
  (q/stroke-weight 2)
  (loop [i 20]
    (when (< i 400)
      (q/line i 0 (+ i (/ i 2)) 80)
      (recur (+ i 20)))))

;; using doseq-range
(defn draw-2 []
  (q/stroke-weight 2)
  (doseq [i (range 20 400 20)]
    (q/line i 0 (+ i (/ i 2.0)) 80)))

(q/defsketch example4_8
  :size [480 120]
  :draw draw-2)
