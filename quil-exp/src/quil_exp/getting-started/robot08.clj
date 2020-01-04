(ns quil-exp.getting-started.robot08
  (:require [quil.core :as q]
            [quil.middleware :as m]
            [quil-exp.getting-started.robot :as rb]))

;; Define a Class
;; Robot 08: Objects.
;; pg. 147.

;; robot robot -> state
(defn make-state [bot-1 bot-2]
  {:bot-1 bot-1  :bot-2 bot-2})

;; none -> state
(defn setup []
  (let [svg-name-1  "resources//robot1.svg"
        svg-name-2  "resources//robot2.svg"
        bot-1  (rb/new-robot svg-name-1 90 80 )
        bot-2  (rb/new-robot svg-name-2 440 30)]
    (make-state bot-1 bot-2)))

;; state -> state
(defn update-state [state]
  (let [bot-1 (rb/update-bot (:bot-1 state))
        bot-2 (rb/update-bot (:bot-2 state))]
    (make-state bot-1 bot-2)))

;; state -> IO
(defn draw [state]
  (q/background 0 153 204)
  (rb/display (:bot-1 state))
  (rb/display (:bot-2 state)))


(q/defsketch robot_08
  :title "Robot 8: Objects"
  :size [720 480]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
