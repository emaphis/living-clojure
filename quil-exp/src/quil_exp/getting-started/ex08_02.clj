(ns quil-exp.getting-started.ex08-02
  (:require [quil.core :as q]))

;; Frames
;; Example 8-02: Set the Frame Rate 
;; pg. 104.

(defn setup []
  ;;(q/frame-rate 30)  ; Thirty frames each second.
  ;;(q/frame-rate 12)  ; Twelve frames each second.
  ;;(q/frame-rate  2)  ; Two frames each second.
  (q/frame-rate 0.5) ; One frame every two seconds.
  )

(defn draw []
  (println (q/current-frame-rate)))


(q/defsketch example8_01
  :title "Set the Frame Rate"
  :setup setup
  :draw draw)
