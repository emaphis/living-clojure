(ns quil-exp.getting-started.ex05-09
  (:require [quil.core :as q]))

;;;Follow
;; Example 5-9: Smooth Lines with Easing
;; pg. 56.

(def x (atom 1.0))
(def easing 0.01)

(defn setup []
  (reset! x 1.0))


(defn draw []
  (let [target-x (q/mouse-x)
        _ (swap! x #(+ % (* (- target-x %) easing)))]
    (q/ellipse @x 40 12 12)
    (println (str (q/mouse-x) " : " @x))))


(q/defsketch example5_9
  :size [220 120]
  :setup setup
  :draw draw)
