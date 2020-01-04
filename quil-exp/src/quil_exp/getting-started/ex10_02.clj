(ns quil-exp.getting-started.ex10-02
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Create Objects
;; Example 10-02: Make Multiple Objects
;; pg. 144.

;;; A Jitterbug

;; Constants
(def SPEED 2.5)

;; float float int -> state
(defn make-jitterbug [x y diameter]
  {:x x  :y y :diameter diameter})

;; bug -> bug
(defn move [bug]
  (let [x   (+ (:x bug) (q/random (* -1 SPEED) SPEED))
        y   (+ (:y bug) (q/random (* -1 SPEED) SPEED))
        dia (:diameter bug)]
    (make-jitterbug x y dia)))

;; but -> IO
(defn display [bug]
  (let [x   (:x bug)
        y   (:y bug)
        dia (:diameter bug)]
    (q/ellipse x y dia dia)))


;;; A Sketch

;; jitterbug jitterbug -> state
(defn make-state [jit bug]
  {:jit jit :bug bug})

;; none -> state
(defn setup []
  (let [jit (make-jitterbug (* (q/width) 0.33) (/ (q/height) 2) 50)
        bug (make-jitterbug (* (q/width) 0.66) (/ (q/height) 2) 10)]
    (make-state jit bug)))

;; state -> state
(defn update-state [state]
  (let [jit (move (:jit state))
        bug (move (:bug state))]
    (make-state jit bug)))

;; state -> IO
(defn draw [state]
  (let [jit (:jit state)
        bug (:bug state)]
    (display jit)
    (display bug)))

(q/defsketch example10_02
  :size [480 120]
  :title "Make Multiple Objects"
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
