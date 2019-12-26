(ns quil-exp.dynamic
  (:require [quil.core :as q]))

;; dynamic workflow
;; https://github.com/quil/quil/wiki/Dynamic-Workflow-(for-REPL)

(defn setup []
  (q/smooth)
  (q/frame-rate 1)
  (q/background 200))

(defn draw []
  ;;(q/stroke (q/random 255)) ; grey
  (q/stroke (q/random 255) (q/random 255) (q/random 255)) ; color
  (q/stroke-weight (q/random 10))
  (q/fill (q/random 255))

  (let [diam (q/random 100)
        x    (q/random (q/width))
        y    (q/random (q/height))]
    (q/ellipse x y diam diam)))

;; reload namespaces.
;; (use :reload 'quil-exp.workflow)
;; (use :reload 'quil-exp.dynamic)
