(ns quil-exp.getting-started.ex07-08
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Fonts
;; Example 7-8: Store Text in a String
;; pg. 96.

(def QUOTE "That's one small step for man...")

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
  (q/text QUOTE 26 24 240 100))


(q/defsketch example7_8
  :title "Store Text in a String"
  :size [480 120]
  :setup setup
  ;;:update update-state
  :draw draw
  :middleware [m/fun-mode])
