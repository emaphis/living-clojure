(ns quil-exp.getting-started.ex11-05
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; From Variables to Arrays
;; Example 11-5: Compact Array Assignment
;; pg. 154.

;; none -> state
(defn make-state []
  {:x  [0 0]})        ; Declare and create the array.

;; none -> state
(defn setup []
  (let [state-1  (make-state)
        state-2  (assoc state-1 0 12)
        state-3  (assoc state-2 1 2)]
    state-3))

(q/defsketch example11_05
  :title "Compact Array Assignment"
  :size [200 200]
  :setup setup
  :middleware [m/fun-mode]) 
