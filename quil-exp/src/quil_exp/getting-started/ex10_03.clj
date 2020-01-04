(ns quil-exp.getting-started.ex10-03
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]
            [quil-exp.getting-started.jitterbug :as jb]))

;; Create Objects
;; Example 10-03: Tabs
;; pg. 146.

;;; A Sketch

;; jitterbug jitterbug -> state
(defn make-state [jit bug]
  {:jit jit :bug bug})

;; none -> state
(defn setup []
  (let [jit (jb/make-jitterbug (* (q/width) 0.33) (/ (q/height) 2) 50)
        bug (jb/make-jitterbug (* (q/width) 0.66) (/ (q/height) 2) 10)]
    (make-state jit bug)))

;; state -> state
(defn update-state [state]
  (let [jit (jb/move (:jit state))
        bug (jb/move (:bug state))]
    (make-state jit bug)))

;; state -> IO
(defn draw [state]
  (let [jit (:jit state)
        bug (:bug state)]
    (jb/display jit)
    (jb/display bug)))

(q/defsketch example10_03
  :size [480 120]
  :title "Tabs"
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
