(ns htdp.ch1-07-predicates
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]
            [clojure.string :refer :all]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Predicates: Know Thy Data

;; testing datatypes with predicates.

(number? 4)
;; => true

(number? Math/PI)
;; => true

(number? true)
;; => false

(number? "fortytwo")
;; => false

(string? "string")
;; => true

(string? 42)
;; => false

(int? 30);; => true
(int? 30.0) ;; => false


;; Ex. 9: Add the following line to the definitions area of DrRacket:

;; Then create an expression that converts the value of in to a positive number.
;; For a String, it determines how long the String is; for an Image, it uses the
;; area; for a Number, it decrements the number by 1, unless it is already 0 or
;; negative; for #true it uses 10 and for #false 20.

(def in "abc")

(if (string? in)
  (count in)
  (if (number? in)
    (if (> in 0)
      (dec in)
      in)
    (if (boolean? in)
      (if in
        10
        20)
      "unknown-type")))
