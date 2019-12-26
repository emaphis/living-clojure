(ns quil-exp.workflow
  (:require [quil.core :as q]
            [quil-exp.dynamic :as dynamic]))

;; example from:
;; https://github.com/quil/quil/wiki/Dynamic-Workflow-(for-REPL)
;; replace 

(q/defsketch example_2
  :title "Example sketch #2"
  :setup dynamic/setup
  :draw dynamic/draw
  :size [323 200])


;; (use :reload 'quil-exp.workflow)
;; (use :reload 'quil-exp.dynamic)

;; calling API functions
;; (quil.applet/with-applet quil-exp.workflow/example_2
;;   (q/random 10))

