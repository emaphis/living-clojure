(ns htdp.ch02.sec02-computing
  (:require [htdp.util :refer :all]
            [clojure.repl :refer :all]
            [clojure.string :refer :all]
            [htdp.ch02.sec01-functions :refer :all]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Computing


(defn opening [first-name last-name]
  (str "Dear " first-name ","))

(opening "Matthew" "Fisler")
;; => "Dear Matthew,"


(defn distance-to-origin [x y]
  (Math/sqrt (+ (* x x) (* y y))))

(distance-to-origin 3 4)
;; => 5.0

(defn string-first' [s]
  (subs s 0 1))

(string-first' "hello world")
;; => "h"
