(ns quil-exp.getting-started.ex10-01
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Create Objects
;; Example 10-01: Make an Object
;; pg. 142.

;; See ex08_01.clj
;; Idiomatic Clojure stores global state in a map, so I've been using a `state` map
;; to store global state. So in 08_01 `state` was the same as a `jitterbug` map in
;; this sketch. Jitterbug state has been separated from global state in this sketch.
;; So state is represented by two maps, a `state` containing a `jitterbug`.


;;; A Jitterbug

;; Constants
(def SPEED 2.5)

;; jitterbug is two floats and an int.
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


;;; A Sketch - state

;; a state contains a jitterbug
;; jitterbug -> state
(defn make-state [bug]
  {:bug bug})

;; none -> state
(defn setup []          ; Create object and pass in parameters
  (let [bug  (make-jitterbug (/ (q/width) 2) (/ (q/height) 2) 20)]
    (make-state bug)))  ; store object in a state

;; state -> state
(defn update-state [state]
  (let [bug (move (:bug state))]
    (make-state bug)))

;; state -> IO
(defn draw [state]
  (let [bug (:bug state)]
    (display bug)))

(q/defsketch example10_01
  :size [480 120]
  :title "Make an Object"
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
