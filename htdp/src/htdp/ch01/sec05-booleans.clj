(ns htdp.ch01.sec05-booleans
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]
            [clojure.string :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Arithmetic of Booleans

true  ;; => true
false ;; => false

(or true true) ;; => true
(or true false) ;; => true
(or false true) ;; => true
(or false false) ;; => false

(and true true) ;; => true
(and true false) ;; => false
(and false true) ;; => false
(and false false) ;; => false

(not true) ;; => false
(not false) ;; => true


;; Ex 7: Boolean expressions can express some everyday problems. Suppose you
;; want to decide whether today is an appropriate day to go to the mall. You go
;; to the mall either if it is not sunny or if  today is Friday (because that
;; is when stores post new sales items).

;; Here is how you could go about it using your new knowledge about Booleans.
;; First add these two lines.

(def sunny true)
(def friday false)

;; Now create an expression that computes whether sunny is false or friday
;; is true. So in this particular case, the answer is #false. (Why?)
(or (not sunny) friday)
;; => false
