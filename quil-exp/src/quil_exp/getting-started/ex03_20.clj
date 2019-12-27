(ns quil-exp.getting-started.ex03-20
  (:require [quil.core :as q]))

;; Custom Shapes
;; Example 3-20: Close the Gap.
;; pg. 29.

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
  (q/end-shape :close))


(q/defsketch example3_20
  :size [480 120]
  :draw draw)
