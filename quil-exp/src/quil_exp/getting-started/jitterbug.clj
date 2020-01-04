(ns quil-exp.getting-started.jitterbug
  (:require [quil.core :as q]))

;; Create Objects
;; Example 10-03: Jitterbug namespace
;; pg. 146.

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
