(ns quil-exp.getting-started.ex11-03
  (:require [quil.core :as q]
            [quil.middlewares.fun-mode :as m]))

;; From Variables to Arrays
;; Example 11-3: Vectors, Not Variables
;; pg. 151.

;; Use lazy sequences instead of arrays or vectors for large collections
;; none -> state
(defn setup []
  (q/no-stroke)
  (q/stroke 150 200)  ; white, alpha
  (take 3000 (repeatedly (fn [] (+ (q/random -1000 200))))))

;; state -> state
(defn update-state [state]
  (map (fn [n] (+ n 0.5)) state))

;; state -> IO
(defn draw [state]
  (q/background 0)
  (doseq [i (range (count state))]
    (let [x (nth state i)
          y (* i 0.4)]
      (q/arc x y 12 12 0.52 5.76))))


(q/defsketch example11_03
  :title "Vectors, Not Variables"
  :size [500 500]
  :setup setup
  :update update-state
  :draw draw
  :middleware [m/fun-mode])
