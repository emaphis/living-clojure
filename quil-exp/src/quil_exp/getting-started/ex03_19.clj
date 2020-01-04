(ns quil-exp.getting-started.ex03-19
  (:require [quil.core :as q]))

;; Custom Shapes
;; Example 3-19: Draw an Arrow
;; pg. 28.

(defn draw []
  (q/begin-shape)
  (q/fill 153 176 180)
  (q/vertex 180 82)
  (q/vertex 207 36)
  (q/vertex 214 63)
  (q/vertex 407 11)
  (q/vertex 412 30)
  (q/vertex 219 82)
  (q/vertex 226 109)
  ;; (q/vertex 180 82) ; finish - see next example
  (q/end-shape))


(q/defsketch example3_19
  :title "Draw an Arrow"
  :size [480 120]
  :draw draw)
