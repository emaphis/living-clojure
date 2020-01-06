(ns quil-exp.getting-started.ex11-06
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; From Variables to Arrays
;; Example 11-5: Compact Array Assignment
;; pg. 154.


(defn make-state []
  {:x [12 2]})  ; Declare and create the array 

;; none -> state
(defn setup []
  (make-state)) ; don't forget - fun mode.  



(q/defsketch example11_06
  :title "Compact Array Assignment"
  :size [200 200]
  :setup setup
  :middleware [m/fun-mode]) 
