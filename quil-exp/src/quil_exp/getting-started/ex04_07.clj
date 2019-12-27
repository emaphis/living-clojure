(ns quil-exp.getting-started.ex04-07
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-7: Flex Your loop-recurs' Muscles
;; pg. 43.

;; for (int i = 20; i < 400; i += 8) {
;;   line(i, 40, i + 60, 80);
;; }

(defn draw []
  (q/stroke-weight 2)
  ;;(q/line 20 40 80 80)  ; +60 +0  +60 +0
  (loop [i 20]
    (when (< i 400)
      (q/line i 40 (+ i 60) 80)
      (recur (+ i 8)))))

;; doseq-range
(defn draw-2 []
  (q/stroke-weight 2)
  (doseq [i (range 20 400 8)]
    (q/line i 40 (+ i 60) 80)))

(q/defsketch example4_7
  :size [480 120]
  :draw draw-2)
