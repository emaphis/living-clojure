(ns quil-exp.getting-started.ex02-01
  (:require [quil.core :as q]))

;; pg. 9 - A basic example.

;;; ellipse(50, 50, 80 80);

(defn setup []
  ())

(defn draw []
  (q/ellipse 50 50 80 80))

(q/defsketch example2_1
  :size [100 100]
  :setup setup
  :draw draw)
