(ns quil-exp.getting-started.ex04-06
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-6: Use a lopp-recure instead of a for.
;; pg. 41.

;; for (int i = 20; i < 400; i += 60) {
;;   line(i, 40, i + 60, 80);
;; }

(defn draw []
  (q/stroke-weight 8)
  ;;(q/line 20 40 80 80)  ; +60 +0  +60 +0
  (loop [i 20]
    (when (< i 400)
      (q/line i 40 (+ i 60) 80)
      (recur (+ i 60)))))

;; Use a doseq-range instead of a for.
(defn draw-2 []
  (q/stroke-weight 8)
  (doseq [i (range 20 400 60)]
    (q/line i 40 (+ i 60) 80)))


(q/defsketch example4_6
  :size [480 120]
  :draw draw-2)
