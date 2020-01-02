(ns quil-exp.getting-started.ex07-07
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Fonts
;; Example 7-7: Drawing Text in a box.
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
  (q/text "That's one small step for man..." 26 24 240 100))


(q/defsketch example7_7
  :title "Drawing Text in a box."
  :size [480 120]
  :setup setup
  ;;:update update-state
  :draw draw
  :middleware [m/fun-mode])
