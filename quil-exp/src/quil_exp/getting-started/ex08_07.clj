(ns quil-exp.getting-started.ex08-07
  (:require [quil.core :as q]))

;; Random
;; Example 8-07: Generate Random Values
;; pg. 109.


(defn draw []
  (let [r  (q/random 0 (q/mouse-x)) ]
    (println r)))

(q/defsketch example8_07
  :title "Generate Random Values"
  :draw draw)
