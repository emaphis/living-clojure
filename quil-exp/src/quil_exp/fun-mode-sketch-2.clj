(ns quil-exp.fun-mode-sketch-2
  (:require [quil.core :as q]
            [quil.middleware :as m]))


;; State is a circle color and an angle.

;; IO -> state
(defn setup []
  (q/frame-rate 30)
  (q/color-mode :hsb)
  ;; return initial state
  {:color 0
   :angle 0})

;; state -> state
(defn update-state
  "Update sketch state changing circle "
  [state]
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)})

;; state -> IO
(defn draw-state [state]
  ;; Clear the sketch by filling it with light-grey color.
  (q/background 240)
  ;; Set circle color.
  (q/fill (:color state) 255 255)
  ;; Calculate x and y coordinates of the cirlce.
  (let [angle (:angle state)
        x  (* 150 (q/cos angle))
        y  (* 150 (q/sin angle))]
    ;; Move the origin point to the center of the sketch.
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      ;; Draw the circle.
      (q/ellipse x y 100 100))))

(q/defsketch fun-mode-sketch-2
  :title "Spinning circle"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])


