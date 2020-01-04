(ns quil-exp.getting-started.robot07
  (:require [quil.core :as q]))

;; Function Basics
;; Robot 07: Functions 
;; pg. 132

;; Constants
(def radius 45)

(defn setup []
  (q/stroke-weight 2)
  (q/ellipse-mode :radius))

(defn draw-robot [x y body-height neck-height]
  (let [ny  (- y body-height neck-height radius)]

    ;;Neck
    (q/stroke 255)  ; White
    (q/line (+ x  2) (- y body-height) (+ x  2) ny)
    (q/line (+ x 12) (- y body-height) (+ x 12) ny)
    (q/line (+ x 22) (- y body-height) (+ x 22) ny)

    ;; Antennae
    (q/line (+ x 12) ny (- x 18) (- ny 43))  ; Small
    (q/line (+ x 12) ny (+ x 42) (- ny 99))  ; Tall
    (q/line (+ x 12) ny (+ x 78) (+ ny 15))  ; Medium

    ;; Body
    (q/no-stroke)
    (q/fill 255 204 0)              ; Orange
    (q/ellipse x (- y 33) 33 33)    ; anti-gravity orb
    (q/fill 0)                      ; Black
    (q/rect (- x 45) (- y body-height) 90 (- body-height 33)) ; Main Body
    (q/fill 255 204 0)
    (q/rect (- x 45) (- y (+ body-height 17)) 96 6)

    ;; Head
    (q/fill 0)     ; Black
    (q/ellipse (+ x 12) ny radius radius)   ; Head
    (q/fill 255)   ; White
    (q/ellipse (+ x 24) (- ny 6) 14 14)     ; eye
    (q/fill 0)     ; Black
    (q/ellipse (+ x 24) (- ny 6) 3 3)       ; Pupil
    (q/fill 153 204 255)  ; Light Blue
    (q/ellipse x (- ny 8) 5 5)              ; small eye
    (q/ellipse (+ x 30) (- ny 26) 4 4)      ; small eye
    (q/ellipse (+ x 41) (+ ny 6) 3 3)))     ; small eye


(defn draw []
  (q/background 0 153 204)
  (draw-robot 120 420 110 140)
  (draw-robot 270 460 260 95)
  (draw-robot 420 310 80 10)
  (draw-robot 570 390 180 40))


(q/defsketch robot_07
  :title "Robot 7: Functions"
  :size [720 480]
  :setup setup
  :draw draw)
