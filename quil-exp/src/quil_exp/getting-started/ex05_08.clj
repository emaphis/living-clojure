(ns quil-exp.getting-started.ex05-08
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Follow
;; Example 5-8: Easing Does It.
;; pg. 54.

;; Use fun-mode to handle updatable state.

(def EASING 0.01)

;; state is map containing :x a float

;; IO -> state
(defn setup []
  {:x 1})

;; state -> state
(defn update-state [state]
  (let [target-x (q/mouse-x)
        x (:x state)
        new-x (+ x (* (- target-x x) EASING))]
    {:x new-x}))

;; state -> IO
(defn draw [state]
  ((let [x (:x state)]
     (q/ellipse x 40 12 12)
     (println ":D :D X=> " x)
     #_(println (str (q/mouse-x) " : " x)))))

(q/defsketch example5_8
  :size [220 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
