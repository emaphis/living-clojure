(ns quil-exp.getting-started.ex08-06
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Tweening
;; Example 8-06: Calculate Tween Positions 
;; pg. 107.


(def START-X 20)   ; Initial x coordinate
(def STOP-X  160)  ; Final x coordinate
(def START-Y  30)  ; Initial y coordinate
(def STOP-Y   80)  ; Final y coordinate
(def STEP  0.005)  ; Size of each step (0.0 to 1.0)

;; number number -> state
(defn make-state [ x y pct]
  {:x x        ; Current x coordinate
   :y y        ; Current y coordinate
   :pct pct})  ; Percentage traveled (0.0 to 1.0)

;; none -> state
(defn setup []
  (q/ellipse-mode :radius)
  (make-state START-X START-Y 0.0))

;; state -> state
(defn update-state [state]
  (let [pct (:pct state)]
    (if (< pct 1.0)
      (let [x   (+ START-X (* (- STOP-X START-X) pct)) 
            y   (+ START-Y (* (- STOP-Y START-Y) pct))
            pct (+ pct STEP)]
        (make-state x y pct))
      state)))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (q/ellipse (:x state) (:y state) 10 10))


(q/defsketch example8_06
  :title "Calculate Tween Positions"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
