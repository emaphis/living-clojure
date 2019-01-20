(ns htdp.ch01.sec06-mixed-boolean
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]
            [clojure.string :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Mixed Arithmetic of Booleans

(def x 2)
(def inverse-of-x (/ 1 x))

(if (= x 0) 0 (/ 1 x))
;; => 1/2

(def x 0)
(if (= x 0) 0 (/ 1 x))
;; => 0

(string=? "green" "yellow")
;; => false
(string=? "green" "green")
;; => true
