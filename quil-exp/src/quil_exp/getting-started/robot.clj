(ns quil-exp.getting-started.robot
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Define a Class
;; Robot 08: Objects.
;; pg. 147.

;; call once per new robot - constructor
;; string number number -> robot
(defn new-robot [svg-name x-pos y-pos]
  {:bot-shape (q/load-shape svg-name)
   :x-pos x-pos
   :y-pos y-pos
   :angle (q/random 0 q/TWO-PI)
   :y-offset 0.0 })

;; robot -> robot
(defn update-bot [bot]
  (let [bot-shape (:bot-shape bot)
        x-pos (:x-pos bot )
        y-pos (:y-pos bot)
        angle (+ (:angle bot) 0.05)
        y-offset (* (q/sin angle) 20)]
    {:bot-shape bot-shape
     :x-pos x-pos
     :y-pos y-pos
     :angle angle
     :y-offset y-offset}))

;; robot -> IO
(defn display [bot]
  (q/shape (:bot-shape bot) (:x-pos bot) (+ (:y-pos bot) (:y-offset bot))))
