(ns quil-exp.getting-started.ex02-01
  (:require [quil.core :as q]))

;; Your First Program
;; Example 2-1: Draw an Ellipse
;; pg. 9.

;;; ellipse(50, 50, 80 80);

(defn setup []
  ())

(defn draw []
  (q/ellipse 50 50 80 80))

(q/defsketch example2_1
  :size [100 100]
  :setup setup
  :draw draw)
