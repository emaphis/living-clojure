(ns quil-exp.getting-started.ex09_08
  (:require [quil.core :as q]))

;; Function Basics
;; Example 09-08: Return a Value
;; pg. 131.

;; float -> float
(defn calculate-mars [w]
  (let [new-weight  (* w 0.38)]
    new-weight))

;; nothing -> IO
(defn setup []
  (let [your-weight  132
        mars-wieght  (calculate-mars your-weight)]
    (println mars-wieght)))

(q/defsketch example9_08
  :title "Returning a Value"
  :size [120 120]
  :setup setup)
