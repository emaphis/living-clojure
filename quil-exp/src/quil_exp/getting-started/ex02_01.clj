(ns exp.getting-started.ex02-01
  (:require [quil.core :as q]))

;; Your First Program
;; Example 2-1: Drawing an Ellipse
;; pg. 9.

;;; ellipse(50, 50, 80 80);


(defn draw []
  (q/ellipse 50 50 80 80))


(q/defsketch example2_1
  :title "Drawing an Ellipse"
  :size [100 100]
  :draw draw)
