(ns quil-exp.getting-started.ex11-04
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; From Variables to Arrays
;; Example 11-4: Declare and Assign an Array
;; pg. 153.


(defn make-state [x] {:x x})

;; none -> state
(defn setup []
  (make-state [0 0]))  ; Create the array.

;;state -> state
(defn update-state [state]
  (let [state-1 (assoc (:x state) 0 12) ; Assign the first valuel
        state-2 (assoc state-1 1 2 )]   ; Assign the second value.
    state-2))


(q/defsketch example11_04
  :title "Declare and Assign an Array"
  :size [200 200]
  :setup setup
  :update update-state
  :middleware [m/fun-mode]) 
