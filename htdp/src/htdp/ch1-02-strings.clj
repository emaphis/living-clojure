(ns htdp.ch1-02-strings
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]
            [clojure.string :refer :all]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Arithmetic of Strings


;; `str` is the equivalent of BSL's `string-append`

(str "what " "lovely " "day" " 4 BSL")
;; => "what lovely day 4 BSL"

(str "Hello " "world!")

;; `str` is similar to `+`

(+ 1 1) ;; => 2
(str "a" "b") ;; => "ab"
(+ 1 2) ;; => 3
(str "ab" "c") ;; => "abc"
(+ 2 2) ;; => 4
(str "a" " ") ;; => "a "


;; Ex 2. Add the following two lines to the definitions area:

(def prefix "hello")
(def suffix "world")

(str prefix "_" suffix)
;; => "hello_world"
