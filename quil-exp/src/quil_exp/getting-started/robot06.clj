(ns quil-exp.getting-started.robot06;
  (:require [quil.core :as q]
            [quil.middleware :as m]))


;; Motion
;; Robot 05 - Robot Motion
;; pg. 118

;; Constants
(def EASING 0.04)
(def radius 45)
(def body-height 153)

(defn make-state [x y neck-height angle]
  {:x x :y y
   :neck-height neck-height
   :angle angle})

;; none -> state
(defn setup []
  (q/ellipse-mode :radius)
  (q/background 0 153 204)
  (make-state 180 400 56 0.0))

;; state -> state
(defn update-state [state]
  (let [x         (+ (:x state) (q/random -4 4))
        y         (+ (:y state) (q/random -4 4))
        angle'    (:angle state)
        neck-ht   (+ 80 (* (q/sin angle') 30))
        angle     (+ angle' 0.05)]
    (make-state x y neck-ht angle)))

;; state -> IO
(defn draw [state]
  (let [x           (:x state)
        y           (:y state)
        neck-height (:neck-height state)
        ny          (- y body-height neck-height radius)]

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
    (q/ellipse (+ x 24) (- ny 6) 3 3)        ; Pupil
    ))


(q/defsketch robot_06
  :title "Robot 6"
  :size [360 480]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
