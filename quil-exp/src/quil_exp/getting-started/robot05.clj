(ns quil-exp.getting-started.robot05
  (:require [quil.core :as q]
            [quil.middleware :as m]))


;; Images, Shapes,
;; Robot 05 - Media.
;; pg. 102.


(def EASING 0.05)

;;  image image image image  number -> state
(defn make-state [bot-1 bot-2 bot-3 landscape offset]
  {:bot-1 bot-1 :bot-2 bot-2 :bot-3 bot-3 :landscape landscape :offset offset})

;; none -> state
(defn setup []
  (let [bot-1      (q/load-shape "resources//robot1.svg")
        bot-2      (q/load-shape "resources//robot2.svg")
        bot-3      (q/load-shape "resources//robot3.svg")
        landscape  (q/load-image "resources//alpine.png")]
    (make-state bot-1 bot-2 bot-3 landscape 0)))

;; state -> state
(defn update-state [state]
  (let [o-offset     (:offset state)
        targ-offset  (q/map-range (q/mouse-y) 0 (q/height) -40 40)
        offset       (+ o-offset (* (- targ-offset o-offset) EASING))]
    (make-state (:bot-1 state)
                (:bot-2 state)
                (:bot-3 state)
                (:landscape state)
                offset)))

;; state -> IO
(defn draw [state]
  (let [offset          (:offset state)
        smaller-offset  (* 0.7 offset)
        smallest-offset (* -0.5 smaller-offset)]

    (q/image (:landscape state) 0 0 720 480)

    (q/shape (:bot-1 state) (+ 85 offset) 65)
    (q/shape (:bot-2 state) (+ 510 smaller-offset) 140 78 248)
    (q/shape (:bot-3 state) (+ 410 smallest-offset) 225 39 124)))


(q/defsketch robot_05
  :title "Robot - Media"
  :size [720 480]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
