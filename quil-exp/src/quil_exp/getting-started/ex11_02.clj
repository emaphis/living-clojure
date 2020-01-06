(ns quil-exp.getting-started.ex11-02
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; From Variables to Arrays
;; Example 11-2: Too Many Variables
;; pg. 150.
;; Update of ex08_03

;; number -> state
(defn make-state [x1 x2 x3 x4 x5]
  {:x1 x1
   :x2 x2
   :x3 x3
   :x4 x4
   :x5 x5})

;; none -> state
(defn setup []
  (q/no-stroke)
  (make-state -10 10 35 18 30))

;; state -> state
(defn update-state [state]
  (let [x1 (+ (:x1 state) 0.5)
        x2 (+ (:x2 state) 0.5)
        x3 (+ (:x3 state) 0.5)
        x4 (+ (:x4 state) 0.5)
        x5 (+ (:x5 state) 0.5)]
    (make-state x1 x2 x3 x4 x5)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [x1 (:x1 state)
        x2 (:x2 state)
        x3 (:x3 state)
        x4 (:x4 state)
        x5 (:x5 state)]
    (q/arc x1 20 20 20 0.52 5.76)
    (q/arc x2 40 20 20 0.52 5.76)
    (q/arc x3 60 20 20 0.52 5.76)
    (q/arc x4 80 20 20 0.52 5.76)
    (q/arc x5 100 20 20 0.52 5.76)))

(q/defsketch example11_02
  :title "To Many Variables"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
