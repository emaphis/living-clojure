(ns quil-exp.getting-started.ex07-01
  (:require [quil.core :as q]))

;; Images
;; Example 7-1: Load an Image
;; pg. 91.

;; use a dynamically typed atom instead of a PImage.

(defn setup []
  (q/set-state! :img (q/load-image "resources//lunar.jpg")))

(defn draw []
  (let [img (q/state :img)]
    (q/image img 0 0)))

(q/defsketch example7_1
  :title "Load an Image"
  :size [480 120]
  :setup setup
  :draw draw)
