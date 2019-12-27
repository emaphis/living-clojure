(ns quil-exp.getting-started.ex04-05
  (:require [quil.core :as q]))

;; Repetition
;; Example 4-5: Do the Same Thing Over ad Over.
;; pg. 41.


(defn draw []
  (q/stroke-weight 8)
  (q/line 20 40 80 80)  ; +60 +0  +60 +0
  (q/line 80 40 140 80)
  (q/line 140 40 200 80)
  (q/line 200 40 260 80)
  (q/line 260 40 320 80)
  (q/line 320 40 380 80)
  (q/line 380 40 440 80))


(q/defsketch example4_5
  :size [480 120]
  :draw draw)
