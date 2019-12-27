(ns quil-exp.getting-started.ex03-12
  (:require [quil.core :as q]))

;; Shape Properties
;; Example 3-12: Set Stroke Caps
;; pg. 21.

(defn draw []
  (q/stroke-weight 24)
  (q/line 60 25 130 95)
  (q/stroke-cap :square)  ; Square the line endings.
  (q/line 160 25 230 95)
  (q/stroke-cap :project) ; Project the line endings
  (q/line 260 25 330 95)
  (q/stroke-cap :round)   ; Round the line endings
  (q/line 360 25 430 95))


(q/defsketch example3_12
:size [480 120]
:draw draw)
