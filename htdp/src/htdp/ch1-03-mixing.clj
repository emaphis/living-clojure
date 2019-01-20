(ns htdp.ch1-03-mixing
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]
            [clojure.string :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Mixing it up.

;; Mixed String and other data functions
;; count, nth, parseInt



;; `count` is BSL's `string-length`
;; String -> Number
(+ (count "hello world") 20)
;; => 31

;; `number->string` -> `str`
(+ (count (str 42)) 2)
;; => 4

;; ------
;; Ex 3: Add the following two lines to the definitions area:

(def str1 "helloworld")
(def i 5)

;; Then create an expression using string primitives that adds "_" at position i.
;; In general this means the resulting string is longer than the original one;
;; here the expected result is "hello_world".

(str (subs str1 0 i) "_" (subs str1 i (count str1)))
;; => "hello_world"

;; --------
;; Ex 4: Use the same setup as in exercise 3 to create an expression that
;; deletes the ith position from str.
;; Clearly this expression creates a shorter string than the given one.
;; Which values for i are legitimate?

(str (subs str1 0 i) (subs str1 (inc i) (count str1)))
;; => "helloorld"
