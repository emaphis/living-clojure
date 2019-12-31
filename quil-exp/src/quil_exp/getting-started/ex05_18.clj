(ns quil-exp.getting-started.ex05-18
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;; Type
;; Example 5-18: Check for Specific Keys.
;; pg. 69.

;; a state is an empty map of a key which is a string

;; string -> state
(defn make-state [key]
  {:key key})

;; IO -> state
(defn setup []
  (q/background 204)
  (make-state "_"))

;; state -> state
(defn update-state[state]
  (make-state (str (q/raw-key))))

;; state -> IO
(defn draw [state]
  (q/background 204)
  (if (q/key-pressed?)
    (let [key (:key state)]
      (if (or (= key "H") (= key "h"))
        (q/line 30 60 90 60))
      (if (or (= key "N") (= key "n"))
        (q/line 30 20 90 100))))
  (q/line 30 20 30 100)
  (q/line 90 20 90 100))

(q/defsketch example5_17
  :title "Check for Specific Keys"
  :size [120 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
