(ns quil-exp.getting-started.ex11-08
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; Repetition and Arrays.
;; Example 11-7: Filling an Array in a for Loop
;; pg. 156.


;; nothing -> state
(defn new-state [width]  ; using a lazy seq
  (take width (repeatedly #(q/random 255))))

;; none -> state
(defn setup []
  (q/no-stroke)
  (new-state (q/width)))

;; state -> state
(defn update-state [state]
  state)  ; no updates

;; state -> IO
(defn draw [state]
  (doseq [i (range (count state))]
    (q/stroke (nth state i))
    (q/line i 0 i (q/height))))

(q/defsketch example11_08
  :title "Filling an Array in a for Loop"
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
