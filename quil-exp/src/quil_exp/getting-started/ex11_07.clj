(ns quil-exp.getting-started.ex11-07
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; From Variables to Arrays
;; Example 11-7: Revisiting the First Example.
;; pg. 154.
;; Update of ex11_01

;; number -> state
(defn new-state []
  {:x [-20 20]})

;; none -> state
(defn setup []
  (q/no-stroke)
  (new-state))

;; state -> state
(defn update-state [state]
  (let [array (:x state)
        x0    (+ (get array 0) 0.5)   ; Increase the first element
        x1    (+ (get array 1) 0.5)]  ; Increase the second element.
    {:x [x0 x1]}))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [x (:x state)]
    (q/arc (get x 0) 30 40 40 0.52 5.76)
    (q/arc (get x 1) 90 40 40 0.52 5.76)))

(q/defsketch example11_07
  :title "Revisiting the First Example"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
