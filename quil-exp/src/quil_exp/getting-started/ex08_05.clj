(ns quil-exp.getting-started.ex08-05
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Speed and Direction
;; Example 8-05: Bouce Off the Wall
;; pg. 107.

(def RADIUS 40)
(def SPEED 0.5)

;; number number -> state
(defn make-state [x direction]
  {:x x :direction direction})

;; none -> state
(defn setup []
  (q/ellipse-mode :radius)
  (make-state 100 1))

;; state -> state
(defn update-state [state]
  (let [sv-dir     (:direction state)
        sv-x       (:x state)
        x          (+ sv-x (* SPEED sv-dir))
        direction  (if (or (> x (+ (q/width) RADIUS))
                           (< x RADIUS))
                     (* -1 sv-dir)  ; flip direction
                     sv-dir)]
    (make-state x direction)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (let [x    (:x state)
        direction (:direction state)]
    (if (= direction 1)
      (q/arc x 60 RADIUS RADIUS 0.52 5.76)
      (q/arc x 60 RADIUS RADIUS 3.67 8.9))))


(q/defsketch example8_05
  :title "Bounce Off the Wall"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
