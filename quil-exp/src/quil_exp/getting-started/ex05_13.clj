(ns quil-exp.getting-started.ex05-13
  (:require [quil.core :as q]
            [quil.middleware :as m]))

;;;Location.
;; Example 5-13: Find the Cursor.
;; pg. 62.

;; state is map with an float x and int 10;


;; IO -> state
(defn setup []
  {:x (/ (q/width) 2) :offset 10})

;; state -> state
(defn update-state [state]
  (let [x (:x state)]
    (cond
      (> (q/mouse-x) x) {:x (+ x 0.5) :offset -10}
      (< (q/mouse-x) x) {:x (- x 0.5) :offset 10}
      :else state)))

;; state -> IO
(defn draw [state]
  (q/background 204)  ; erase object
  ;; Draw arrow left or right depending on `offset` value.
  (q/line (:x state) 0 (:x state) (q/height))
  (q/line (q/mouse-x) (q/mouse-y) (+ (qxf/mouse-x) (:offset state)) (- (q/mouse-y) 10))
  (q/line (q/mouse-x) (q/mouse-y) (+ (q/mouse-x) (:offset state)) (+ (q/mouse-y) 10))
  (q/line (q/mouse-x) (q/mouse-y) (+ (q/mouse-x) (* (:offset state) 3)) (q/mouse-y)))


(q/defsketch example5_13
  :size [240 120]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
