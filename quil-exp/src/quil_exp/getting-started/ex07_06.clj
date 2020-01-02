(ns quil-exp.getting-started.ex07-06
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Fonts
;; Example 7-6:  Drawing with Fonts
;; pg. 96.

;; image number -> state
(defn make-state [font]
  {:font font})

;; none -> state
(defn setup []
  (let [font (q/create-font "resources//SourceCodePro-Regular.ttf" 24)]
    (q/text-font font)
    (make-state font)))

;; state -> state
#_(defn update-state [state]
    state)

;; state -> IO
(defn draw [state]
  (q/background 102)
  (q/fill 255)
  (q/text-size 32)
  (q/text "That's one small step for man..." 25 60)
  (q/text-size 16)
  (q/text "That's one small step for man..." 27 90))

(q/defsketch example7_6
  :title "Drawing with Fonts "
  :size [480 120]
  :setup setup
  ;;:update update-state
  :draw draw
  :middleware [m/fun-mode])
