(ns quil-exp.getting-started.ex05-09
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Follow
;; Example 5-9: Smooth Lines with Easing
;; pg. 56.

(def EASING 0.05)

;; states is a map with :x :y
(defn make-state [x y px py weight]
  {:x x :y y :px px :py py :weight weight})

;; IO -> state 
(defn setup []
  (q/stroke 0 102)
  (make-state 1 1 1 1 1))

;; IO, state -> state
(defn update-state [state]
  (let [px (:x state)
        py (:y state)
        x  (+ px (* (- (q/mouse-x) px) EASING))
        y  (+ py (* (- (q/mouse-y) py) EASING))
        wt (q/dist px py x y)]
    (make-state x y px py wt)))

;; state -> IO
(defn draw-state [state]
  (q/stroke-weight (:weight state))
  (q/line (:x state) (:y state) (:px state) (:py state)))


(q/defsketch example5_9
  :size [480 120]
  :setup setup
  :update update-state
  :draw draw-state
  :middleware [m/fun-mode])
