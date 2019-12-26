(ns quil-exp.core
  (:require [quil.core :as q]
            [quil.middleware :as m])
  (:gen-class))

(defn setup []
  ;; Set from rate to 30 frames per second.
  (q/frame-rate 30)
  ;; Set color mored to HSB (HSV) instead of RGB.
  (q/color-mode :hsb)
  ;; setup function returns initial state. It contains
  ;; circle color and position
  {:color 0
   :angle 0} )

(defn update-state [state]
  ;; Update sketch state by changing circle color and position
  {:color (mod (+ (:color state) 0.7) 255)
   :angle (+ (:angle state) 0.1)} )

(defn draw-state [state]
  ;; clear the sketch by filling it with light-grey color.
  (q/background 240)
  ;; set circle color.
  (q/fill (:color state) 255 255)
  ;; Calculate x and y coordinates of the circle
  (let [angle (:angle state)
        x  (* 150 (q/cos angle))
        y  (* 150 (q/sin angle))]
    ;; Have origin point to the center of the sketch
    (q/with-translation [(/ (q/width) 2)
                         (/ (q/height) 2)]
      (q/ellipse x y 100 100))))

(q/defsketch quil-exp
  :title "You spin my circle right round"
  :size [500 500]
  ;; setup function called only once, during sketch initialization
  :setup setup
  ;; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  ;; This sketch uses functional-mode middleware.
  ;; Check quil wiki for more info about middlewares and particularly
  ;; fun-mode.
  :middleware [m/fun-mode]  )

;; to start -
;;(use 'quil-exp.core :reload-all true)

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
