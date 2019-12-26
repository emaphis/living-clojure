(ns quil-exp.quil-intro
  (:require [quil.core :as q]))

;;; example from:
;;;  http://nbeloglazov.com/2014/05/29/quil-intro.html

;; define 'f'
;; You can get awesome plots using random combinations of
;; trigonometric functions.

;; (defn f [t]
;;   [(* t (q/sin t))
;;    (* t (q/cos t))])

;;  - here `f` plots a flower.
(defn f [t]
  (let [r (* 200 (q/sin t) (q/cos t))]
    [(* r (q/sin (* t 0.2)))
     (* r (q/cos (* t 0.2)))]))


(defn draw-plot [f from to step]
  (doseq [two-points (->> (range from to step)
                          (map f)
                          (partition 2 1))]
    ;; we could use `point`
    ;; but let's rather draw a lie which connects 2 points
    ;; of the plot
    (apply q/line two-points)))


;; define the function which draws a spiral.
(defn draw []
  ;; make background white
  (q/with-translation [(/ (q/width) 2) (/ (q/height) 2)]
    (let [t (/ (q/frame-count) 10)]
      (q/line (f t)
              (f (+ t 0.1))))))

(defn setup []
  ;; call draw 60 times per seconed
  (q/frame-rate 60)
  (q/background 255))


;; run sketch
(q/defsketch trigonometry
  :size [300 300]
  :setup setup
  :draw draw)
