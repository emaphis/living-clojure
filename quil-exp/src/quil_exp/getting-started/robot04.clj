(ns quil-exp.getting-started.robot04
  (:require [quil.core :as q]
            [quil.middleware :as m]))


;; Translation 
;; Robot 04 - Translate, Rotate, Scale 
;; pg. 85.

(def EASING 0.04)

;;  number number -> state
(defn make-state [mouse-x scaler]
  {:mouse-x mouse-x
   :scaler scaler})

;; none -> state
(defn setup []
  (q/ellipse-mode :radius)
  (make-state 0 0.6))

;; state -> state
(defn update-state [state]
  (let [mouse-x   (q/mouse-x)
        scaler    (if (q/mouse-pressed?) 1.0 0.6)]
    (make-state mouse-x scaler)))

;; state -> IO
(defn draw [state]
  (q/stroke-weight 2)
  (q/background 0 153 204)

  (let [x           60
        y           440
        mouse-x     (:mouse-x state)
        body-height 180
        neck-height 40
        scaler      (:scaler state)
        radius      45
        easing      0.04
        neck-y      (* -1 (+ body-height neck-height radius))
        angle       (* -1 (/ q/PI 30.0))]

    (q/translate mouse-x y)
    (q/scale scaler)

    ;; Body
    (q/no-stroke)
    (q/fill 255 204 0)          ; Orange
    (q/ellipse 0 -33 33 33)     ; anti-gravity orb
    (q/fill 0)                  ; Black
    (q/rect -45  (* -1 body-height) 90 (- body-height 33)) ; Main Body

    ;;Neck
    (q/stroke 255)  ; White
    (q/line 12 (* -1 body-height) 12 neck-y)

    ;; Hair
    (q/push-matrix)
    (q/translate 12 neck-y)
    (doseq [_ (range 31)]
      (q/line 80 0 0 0)
      (q/rotate angle))
    (q/pop-matrix)

    ;; Head
    (q/no-stroke)
    (q/fill 0)     ; Black
    (q/ellipse 12 neck-y radius radius) ; Head
    (q/fill 255)   ; White
    (q/ellipse 24 (- neck-y 6) 14 14)   ; eye
    (q/fill 0)     ; Black
    (q/ellipse 24 (- neck-y 6) 3 3)     ; Pupil
    ))


(q/defsketch robot_04
  :title "Translate, Rotate, Scale"
  :size [360 480]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
