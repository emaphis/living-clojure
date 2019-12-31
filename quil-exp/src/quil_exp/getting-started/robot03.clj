(ns quil-exp.getting-started.robot03
  (:require [quil.core :as q]
            [quil.middleware :as m]))


;; Response
;; Robot 03 - P5
;; pg. 73

(def EASING 0.04)

(defn make-state [x y body-height neck-height neck-y radius]
  {:x x :y y
   :body-height body-height
   :neck-height neck-height
   :neck-y neck-y
   :radius radius})

;; none -> state
(defn setup []
  (q/ellipse-mode :radius)
  (make-state 60 440 160 70 0 45))

;; state -> state
(defn update-state [state]
  (let [target-x  (q/mouse-x)
        old-x     (:x state)
        x         (+ old-x (* (- target-x old-x) EASING))
        y         (:y state)
        body-ht   (if (q/mouse-pressed?) 90 160)
        neck-ht   (if (q/mouse-pressed?) 16 70)
        radius    (:radius state)
        neck-y    (- y body-ht neck-ht radius)]
    (make-state x y body-ht neck-ht neck-y radius)))

;; state -> IO
(defn draw [state]
  (q/stroke-weight 2)
  (q/background 0 153 204)

  (let [x           (:x state)
        y           (:y state)
        body-height (:body-height state)
        neck-height (:neck-height state)
        radius      (:radius state)
        neck-y      (:neck-y state)]

    (q/stroke-weight 2)
    (q/background 0 153 204)

    ;;Neck
    (q/stroke 255)  ; White
    (q/line (+ x 12) (- y body-height) (+ x 12) neck-y)

    ;; Antennae
    (q/line (+ x 12) neck-y (- x 18) (- neck-y 43))  ; Small
    (q/line (+ x 12) neck-y (+ x 42) (- neck-y 99))  ; Tall
    (q/line (+ x 12) neck-y (+ x 78) (+ neck-y 15))  ; Medium

    ;; Body
    (q/no-stroke)
    (q/fill 255 204 0)              ; Orange
    (q/ellipse x (- y 33) 33 33)    ; anti-gravity orb
    (q/fill 0)                      ; Black
    (q/rect (- x 45) (- y body-height) 90 (- body-height 33)) ; Main Body

    ;; Head
    (q/fill 0)   ; Black
    (q/ellipse (+ x 12) neck-y radius radius) ; Head
    (q/fill 255)
    (q/ellipse (+ x 24) (- neck-y 6) 14 14)   ; eye
    (q/fill 0)     ; Black
    (q/ellipse (+ x 24) (- neck-y 6) 3 3)      ; Pupil
    ))


(q/defsketch robot_03
  :title "Robot 3"
  :size [360 480]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
